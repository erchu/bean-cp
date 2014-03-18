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

import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Defines mapping between source and destination class. Class is not thread safe. Source and
 * destination classes must have default public or private constructor.
 *
 * @param <S> source class
 * @param <D> destination class
 *
 * @author Rafal Chojnacki
 */
class MapImpl<S, D> implements Map<S, D> {

    private static enum MapMode {

        CONFIGURATION,
        EXECUTION
    }

    private final Class<S> sourceClass;

    private final Class<D> destinationClass;

    private final MapConfiguration<S, D> configuration;

    private ObjectsReferenceImpl<S, D> mapObjectsReference;

    private MapMode mode = MapMode.CONFIGURATION;

    public MapImpl(final Class<S> sourceClass, final Class<D> destinationClass,
            final MapConfiguration<S, D> configuration) {
        this.configuration = configuration;
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    void configure() {
        if (mode != MapMode.CONFIGURATION) {
            throw new IllegalStateException("Map was already configured.");
        }

        if (Modifier.isFinal(destinationClass.getModifiers())) {
            throw new MapConfigurationException(
                    String.format("Destination class %s cannot be final.",
                            destinationClass.getName()));
        }

        FakeObjectBuilder proxyBuilder = new FakeObjectBuilder();
        S sourceObject = proxyBuilder.createFakeObject(sourceClass);
        D destinationObject = proxyBuilder.createFakeObject(destinationClass);

        // Source and destination object instances are not required by MapImpl in CONFIGURATION
        // mode, but Java lambda handling mechanizm requires non-null value, so we need to create
        // proxy instance. Unfortunatelly this enforces constraint on source and destination
        // classes: they must have default public or protected constructor.
        mapObjectsReference = new ObjectsReferenceImpl<>(sourceObject, destinationObject);
        configuration.apply(this, mapObjectsReference);

        mode = MapMode.EXECUTION;
        mapObjectsReference = null;
    }

    void execute(S source, D destination) {
        if (mode != MapMode.EXECUTION) {
            throw new IllegalStateException("Map is not configure. Use configure() first.");
        }

        configuration.apply(this, new ObjectsReferenceImpl<>(source, destination));
    }

    Class<S> getSourceClass() {
        return sourceClass;
    }

    Class<D> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public Map<S, D> afterAnyMemberMap(Consumer<String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> afterMap(Action action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> afterMemberMap(Consumer<T> destinationMember, Action action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> beforeAnyMemberMap(Consumer<String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> beforeMap(Action action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> beforeMemberMap(Consumer<T> destinationMember, Action action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> constructDestinationObjectUsing(Supplier<D> constructor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> constructDestinationObjectUsing(Function<S, D> constructor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> convertUsing(Action action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> enableAnnotationMapping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> setOption(Consumer<T> destinationMember, BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MapImpl<S, D> useConvention(final MappingConvention mappingConvention) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> bindOneToOne(
            final Supplier<T> fromMember,
            final Consumer<T> toMember,
            final BindingOption... options) {
        if (fromMember == null) {
            throw new NullParameterException("fromMember");
        }

        if (toMember == null) {
            throw new NullParameterException("toMember");
        }

        //TODO: Annotations propagation from source to destination
        if (mode == MapMode.EXECUTION) {
            toMember.accept(fromMember.get());
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> bindFunction(
            final Supplier<T> supplierFunction,
            final Consumer<T> toMember,
            final BindingOption... options) {
        if (supplierFunction == null) {
            throw new NullParameterException("supplierFunction");
        }

        if (toMember == null) {
            throw new NullParameterException("toMember");
        }

        if (mode == MapMode.EXECUTION) {
            toMember.accept(supplierFunction.get());
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> bindConstant(
            final T constantValue,
            final Consumer<T> toMember,
            final BindingOption... options) {
        if (toMember == null) {
            throw new NullParameterException("toMember");
        }

        if (mode == MapMode.EXECUTION) {
            toMember.accept(constantValue);
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> verifyAllDestinationPropertiesConfigured() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<D, S> withReverseMap(ReverseMapOption reverseMapOption) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
