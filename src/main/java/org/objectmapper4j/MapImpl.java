/*
 * ObjectMapper4j
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
package org.objectmapper4j;

import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
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
    public MapImpl<S, D> useConvention(final MappingConvention mappingConvention) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> bindFunction(
            final Supplier<T> from,
            final Consumer<T> to,
            final BindingOption... options) {
        if (from == null) {
            throw new NullParameterException("from");
        }

        if (to == null) {
            throw new NullParameterException("to");
        }

        if (mode == MapMode.EXECUTION) {
            to.accept(from.get());
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> bindOneToOne(
            final Supplier<T> from,
            final Consumer<T> to,
            final BindingOption... options) {
        if (from == null) {
            throw new NullParameterException("from");
        }

        if (to == null) {
            throw new NullParameterException("to");
        }

        //TODO: Annotations propagation from source to destination
        if (mode == MapMode.EXECUTION) {
            to.accept(from.get());
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> bindConstant(
            final T constantValue,
            final Consumer<T> to,
            final BindingOption... options) {
        if (to == null) {
            throw new NullParameterException("to");
        }

        if (mode == MapMode.EXECUTION) {
            to.accept(constantValue);
        }

        //TODO: Options parameter processing
        return this;
    }

    @Override
    public <T> Map<S, D> bindByConvention(
            final BiConsumer<D, T> member,
            final MappingConvention convention,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> setOption(
            final BiConsumer<D, T> member,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> verifyAllDestinationPropertiesConfigured() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> beforeMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> afterMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> beforeMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<S, D> afterMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> beforeMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> afterMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> convertUsing(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> constructDestinationObjectUsing(final Supplier<D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<S, D> constructDestinationObjectUsing(final Function<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<D, S> withReverseMap(final ReverseMapOption reverseMapOption) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
