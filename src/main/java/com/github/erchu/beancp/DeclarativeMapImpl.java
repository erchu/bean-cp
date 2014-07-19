/*
 * bean-cp
 * Copyright (c) 2014, Rafal Chojnacki, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.github.erchu.beancp;

import java.util.function.Consumer;
import java.util.function.Supplier;
import static org.apache.commons.lang3.Validate.*;

/**
 * Default implementation of {@link DeclarativeMap} interface.
 *
 * @param <S> source class
 * @param <D> destination class
 */
final class DeclarativeMapImpl<S, D> implements DeclarativeMap<S, D>, MappingExecutor<S, D> {

    private static enum MapMode {

        CONFIGURATION,
        EXECUTION
    }

    private final String INVALID_STATEMENT_ORDER_MESSAGE = "Invalid statement order. Check "
            + DeclarativeMap.class.getSimpleName() + " interface API documentation for details.";

    private final Class<S> _sourceClass;

    private final Class<D> _destinationClass;

    private final DeclarativeMapSetup<S, D> _configuration;

    private Supplier<D> _destinationObjectBuilder;

    private MapMode mode = MapMode.CONFIGURATION;

    private boolean _constructDestinationObjectUsingExecuted;

    private boolean _beforeMapExecuted;

    private boolean _useConventionExecuted;

    private boolean _bindBindConstantOrMapExecuted;

    private boolean _afterMapExecuted;

    private Mapper _executionPhaseMapper;

    private MapConventionExecutor _executionPhaseMapConvention;

    private final ThreadLocal<S> _executionPhaseSourceReference = new ThreadLocal<>();

    private final ThreadLocal<D> _executionPhaseDestinationReference = new ThreadLocal<>();

    private MappingInfo _configurationPhaseMappingsInfo;

    public DeclarativeMapImpl(final Class<S> sourceClass, final Class<D> destinationClass,
            final DeclarativeMapSetup<S, D> configuration) {
        _configuration = configuration;
        _sourceClass = sourceClass;
        _destinationClass = destinationClass;
    }

    @Override
    public <T> DeclarativeMap<S, D> bind(
            final Supplier<T> fromFunction,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options) {
        notNull(fromFunction, "fromFunction");
        notNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (_afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            _bindBindConstantOrMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            boolean map = shouldBeMapped(options);

            if (map) {
                T getValue = fromFunction.get();

                if (getValue == null) {
                    for (BindingOption<S, D, T> i : options) {
                        if (i.getNullSubstitution() != null) {
                            getValue = i.getNullSubstitution();
                            break;
                        }
                    }
                }

                toMember.accept(getValue);
            }
        }

        return this;
    }

    @Override
    public <T> DeclarativeMap<S, D> bindConstant(
            final T constantValue,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options) {
        notNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (_afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            _bindBindConstantOrMapExecuted = true;
        }

        if (mode == MapMode.CONFIGURATION) {
            for (BindingOption<S, D, T> i : options) {
                if (i.getNullSubstitution() != null) {
                    throw new MapperConfigurationException(
                            "Null substitution option not allowed for bindConstant.");
                }
            }
        }

        if (mode == MapMode.EXECUTION) {
            boolean map = shouldBeMapped(options);

            if (map) {
                toMember.accept(constantValue);
            }
        }

        return this;
    }

    @Override
    public <SI, DI> DeclarativeMap<S, D> mapInner(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options) {
        return mapInner(supplierFunction, toMember, null, toMemberClass, options);
    }

    @Override
    public <SI, DI> DeclarativeMap<S, D> mapInner(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Supplier<DI> toMemberGetter,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options) {
        notNull(supplierFunction, "supplierFunction");
        notNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (_afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            _bindBindConstantOrMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            SI currentSourceValue = supplierFunction.get();

            if (currentSourceValue == null) {
                toMember.accept(null);
            } else {
                DI currentDestinationMemberValue;

                if (toMemberGetter == null) {
                    currentDestinationMemberValue = null;
                } else {
                    currentDestinationMemberValue = toMemberGetter.get();
                }

                if (currentDestinationMemberValue == null) {
                    DI mapResult = _executionPhaseMapper.map(currentSourceValue, toMemberClass);
                    toMember.accept(mapResult);
                } else {
                    _executionPhaseMapper.map(currentSourceValue, currentDestinationMemberValue);
                }
            }
        }

        return this;
    }

    @Override
    public DeclarativeMapImpl<S, D> useConvention(final MapConvention mapConvention) {
        notNull(mapConvention, "mapConvention");

        MapConventionExecutor conventionExecutor = new MapConventionExecutor(mapConvention);

        if (mode == MapMode.CONFIGURATION) {
            if (_useConventionExecuted) {
                throw new MapperConfigurationException("useConventionExecuted() cannot be called "
                        + "more than once.");
            }

            if (_bindBindConstantOrMapExecuted || _afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            // Build and cache result
            conventionExecutor.build(_configurationPhaseMappingsInfo, _sourceClass, _destinationClass);
            _executionPhaseMapConvention = conventionExecutor;

            _useConventionExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            // use cached convention
            _executionPhaseMapConvention.map(_executionPhaseMapper,
                    _executionPhaseSourceReference.get(), _executionPhaseDestinationReference.get());
        }

        return this;
    }

    @Override
    public DeclarativeMap<S, D> beforeMap(final Action action) {
        return beforeMap(notUsed -> action.invoke());
    }

    @Override
    public DeclarativeMap<S, D> beforeMap(final Consumer<Mapper> action) {
        if (mode == MapMode.CONFIGURATION) {
            if (_useConventionExecuted || _bindBindConstantOrMapExecuted || _afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            _beforeMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            action.accept(_executionPhaseMapper);
        }

        return this;
    }

    @Override
    public DeclarativeMap<S, D> afterMap(final Action action) {
        return afterMap(notUsed -> action.invoke());
    }

    @Override
    public DeclarativeMap<S, D> afterMap(final Consumer<Mapper> action) {
        if (mode == MapMode.CONFIGURATION) {
            _afterMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            action.accept(_executionPhaseMapper);
        }

        return this;
    }

    @Override
    public DeclarativeMap<S, D> constructDestinationObjectUsing(
            final Supplier<D> destinationObjectBuilder) {
        notNull(destinationObjectBuilder, "destinationObjectBuilder");

        if (mode == MapMode.CONFIGURATION) {
            if (_beforeMapExecuted || _useConventionExecuted || _bindBindConstantOrMapExecuted
                    || _afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            if (_constructDestinationObjectUsingExecuted) {
                throw new MapperConfigurationException("constructDestinationObjectUsing() cannot "
                        + "be called more than once.");
            }

            _constructDestinationObjectUsingExecuted = true;
        }

        setDestinationObjectBuilder(destinationObjectBuilder);

        return this;
    }

    void configure(MappingInfo configurationPhaseMappingsInfo) {
        if (mode != MapMode.CONFIGURATION) {
            throw new IllegalStateException("Map was already configured.");
        }

        FakeObjectBuilder proxyBuilder = new FakeObjectBuilder();
        S sourceObject = proxyBuilder.createFakeObject(_sourceClass);
        D destinationObject = proxyBuilder.createFakeObject(_destinationClass);

        _beforeMapExecuted = _bindBindConstantOrMapExecuted = _afterMapExecuted = false;
        _configurationPhaseMappingsInfo = configurationPhaseMappingsInfo;

        // Source and destination object instances are not required by DeclarativeMapImpl 
        // in CONFIGURATION mode, but Java lambda handling mechanizm requires 
        // non-null value, so we need to create proxy instance. Unfortunatelly
        // this enforces constraint on source and destination classes as in javadoc.
        _configuration.apply(this, sourceObject, destinationObject);

        // release reference
        _configurationPhaseMappingsInfo = null;

        mode = MapMode.EXECUTION;
    }

    void execute(final Mapper caller, final S source, final D destination) {
        if (mode != MapMode.EXECUTION) {
            throw new IllegalStateException(
                    "Map is not configured. Use configure() first.");
        }

        _executionPhaseMapper = caller;

        try {
            _executionPhaseSourceReference.set(source);
            _executionPhaseDestinationReference.set(destination);

            _configuration.apply(this, source, destination);
        } finally {
            _executionPhaseSourceReference.set(null);
            _executionPhaseDestinationReference.set(null);
        }
    }

    @Override
    public Class<S> getSourceClass() {
        return _sourceClass;
    }

    @Override
    public Class<D> getDestinationClass() {
        return _destinationClass;
    }

    Supplier<D> getDestinationObjectBuilder() {
        return _destinationObjectBuilder;
    }

    void setDestinationObjectBuilder(final Supplier<D> destinationObjectBuilder) {
        notNull(destinationObjectBuilder, "destinationObjectBuilder");

        _destinationObjectBuilder = destinationObjectBuilder;
    }

    private <T> boolean shouldBeMapped(final BindingOption<S, D, T>[] options) {
        boolean map = true;

        for (BindingOption<S, D, T> i : options) {
            if (i.getMapWhenCondition() != null && i.getMapWhenCondition().get() == false) {
                map = false;
                break;
            }
        }

        return map;
    }
}
