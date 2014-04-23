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
package org.beancp;

import java.util.function.Consumer;
import java.util.function.Supplier;
import static org.beancp.NullParameterException.failIfNull;

/**
 * Defines mapping between source and destination class. Class is not thread safe. Source and
 * destination classes must have default public or private constructor.
 *
 * @param <S> source class
 * @param <D> destination class
 */
final class MapImpl<S, D> extends MappingExecutor<S, D> implements Map<S, D> {

    private static enum MapMode {

        CONFIGURATION,
        EXECUTION
    }

    private final String INVALID_STATEMENT_ORDER_MESSAGE = "Invalid statement order. Check "
            + Map.class.getSimpleName() + " interface API documentation for details.";

    private final Class<S> sourceClass;

    private final Class<D> destinationClass;

    private final MapSetup<S, D> configuration;

    private MapMode mode = MapMode.CONFIGURATION;

    private boolean constructDestinationObjectUsingExecuted;

    private boolean beforeMapExecuted;

    private boolean useConventionExecuted;

    private boolean bindBindConstantOrMapExecuted;

    private boolean afterMapExecuted;

    private Mapper executionPhaseMapper;

    private MappingConvention executionPhaseMappingConvention;

    private ThreadLocal<S> executionPhaseSourceReference = new ThreadLocal<S>();

    private ThreadLocal<D> executionPhaseDestinationReference = new ThreadLocal<D>();

    private MappingsInfo configurationPhaseMappingsInfo;

    public MapImpl(final Class<S> sourceClass, final Class<D> destinationClass,
            final MapSetup<S, D> configuration) {
        this.configuration = configuration;
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
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

    void configure(MappingsInfo configurationPhaseMappingsInfo) {
        if (mode != MapMode.CONFIGURATION) {
            throw new IllegalStateException("Map was already configured.");
        }

        FakeObjectBuilder proxyBuilder = new FakeObjectBuilder();
        S sourceObject = proxyBuilder.createFakeObject(sourceClass);
        D destinationObject = proxyBuilder.createFakeObject(destinationClass);

        beforeMapExecuted = bindBindConstantOrMapExecuted = afterMapExecuted = false;
        this.configurationPhaseMappingsInfo = configurationPhaseMappingsInfo;

        // Source and destination object instances are not required by MapImpl 
        // in CONFIGURATION mode, but Java lambda handling mechanizm requires 
        // non-null value, so we need to create proxy instance. Unfortunatelly
        // this enforces constraint on source and destination classes as in javadoc.
        configuration.apply(this, sourceObject, destinationObject);

        // release reference
        this.configurationPhaseMappingsInfo = null;

        mode = MapMode.EXECUTION;
    }

    @Override
    D execute(final Mapper caller, final S source, final Class<D> destinationClass) {
        D destination = constructDestinationObject(destinationClass);

        execute(caller, source, destination);

        return destination;
    }

    @Override
    void execute(final Mapper caller, final S source, final D destination) {
        if (mode != MapMode.EXECUTION) {
            throw new IllegalStateException(
                    "Map is not configured. Use configure() first.");
        }

        this.executionPhaseMapper = caller;

        try {
            this.executionPhaseSourceReference.set(source);
            this.executionPhaseDestinationReference.set(destination);

            configuration.apply(this, source, destination);
        } finally {
            this.executionPhaseSourceReference.set(null);
            this.executionPhaseDestinationReference.set(null);
        }
    }

    @Override
    Class<S> getSourceClass() {
        return sourceClass;
    }

    @Override
    Class<D> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public <T> Map<S, D> bind(
            final Supplier<T> fromFunction,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options) {
        failIfNull(fromFunction, "fromFunction");
        failIfNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            bindBindConstantOrMapExecuted = true;
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
    public <T> Map<S, D> bindConstant(
            final T constantValue,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options) {
        failIfNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            bindBindConstantOrMapExecuted = true;
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
    public <SI, DI> Map<S, D> mapInner(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options) {
        return mapInner(supplierFunction, toMember, null, toMemberClass, options);
    }

    @Override
    public <SI, DI> Map<S, D> mapInner(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Supplier<DI> toMemberGetter,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options) {
        failIfNull(supplierFunction, "supplierFunction");
        failIfNull(toMember, "toMember");

        if (mode == MapMode.CONFIGURATION) {
            if (afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            bindBindConstantOrMapExecuted = true;
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
                    DI mapResult = executionPhaseMapper.map(currentSourceValue, toMemberClass);
                    toMember.accept(mapResult);
                } else {
                    executionPhaseMapper.map(currentSourceValue, currentDestinationMemberValue);
                }
            }
        }

        return this;
    }

    @Override
    public MapImpl<S, D> useConvention(final MappingConvention mappingConvention) {
        failIfNull(mappingConvention, "mappingConvention");

        if (mode == MapMode.CONFIGURATION) {
            if (useConventionExecuted) {
                throw new MapperConfigurationException("useConventionExecuted() cannot be called "
                        + "more than once.");
            }

            if (bindBindConstantOrMapExecuted || afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            // Build and cache result
            mappingConvention.build(configurationPhaseMappingsInfo, sourceClass, destinationClass);
            this.executionPhaseMappingConvention = mappingConvention;

            useConventionExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            // use cached convention
            this.executionPhaseMappingConvention.execute(executionPhaseMapper,
                    executionPhaseSourceReference.get(), executionPhaseDestinationReference.get());
        }

        return this;
    }

    @Override
    public Map<S, D> beforeMap(final Action action) {
        if (mode == MapMode.CONFIGURATION) {
            if (useConventionExecuted || bindBindConstantOrMapExecuted || afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            beforeMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            action.invoke();
        }

        return this;
    }

    @Override
    public Map<S, D> afterMap(final Action action) {
        if (mode == MapMode.CONFIGURATION) {
            afterMapExecuted = true;
        }

        if (mode == MapMode.EXECUTION) {
            action.invoke();
        }

        return this;
    }

    @Override
    public Map<S, D> constructDestinationObjectUsing(
            final Supplier<D> destinationObjectBuilder) {
        failIfNull(destinationObjectBuilder, "destinationObjectBuilder");

        if (mode == MapMode.CONFIGURATION) {
            if (beforeMapExecuted || useConventionExecuted || bindBindConstantOrMapExecuted
                    || afterMapExecuted) {
                throw new MapperConfigurationException(INVALID_STATEMENT_ORDER_MESSAGE);
            }

            if (constructDestinationObjectUsingExecuted) {
                throw new MapperConfigurationException("constructDestinationObjectUsing() cannot "
                        + "be called more than once.");
            }

            constructDestinationObjectUsingExecuted = true;
        }

        setDestinationObjectBuilder(destinationObjectBuilder);

        return this;
    }
}
