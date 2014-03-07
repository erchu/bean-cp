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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Defines mapping between source and destination class. Class is not thread safe.
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

    private MapMode mode = MapMode.CONFIGURATION;

    public MapImpl(final Class<S> sourceClass, final Class<D> destinationClass,
            final MapConfiguration<S, D> configuration) {
        this.configuration = configuration;
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    void configure() {
        if (mode == MapMode.EXECUTION) {
            throw new IllegalStateException("Map was already configured.");
        }

        configuration.apply(this, (S) null, (D) null);

        mode = MapMode.EXECUTION;
    }

    void execute(S source, D destination) {
        if (mode != MapMode.EXECUTION) {
            throw new IllegalStateException("Map is not configure. Use configure() first.");
        }

        configuration.apply(this, source, destination);
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

    /**
     * Adds mapping between source and destination class for single destination member.
     *
     * @param <T> value data type
     * @param from source class member
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    @Override
    public <T> Map<S, D> bind(
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

    /**
     * Adds mapping between source and destination class for single destination member.
     *
     * @param <T> value data type
     * @param constantValue constant value
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
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
