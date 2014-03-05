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
public abstract class Map<S, D> {

    private final Class<S> sourceClass;

    private final Class<D> destinationClass;

    private MapMode mode = MapMode.CONFIGURATION;

    public Map() {
        Class[] genericSuperclasses = ClassUtils.getGenericSuperclasses(this.getClass());

        this.sourceClass = genericSuperclasses[0];
        this.destinationClass = genericSuperclasses[1];
    }

    /**
     * Defines map configuration. Implementation must be thread safe and has no side effects other
     * that binding definition. Method could be called more than once.
     *
     * @param source source object, null value must be allowed and can't cause exception
     * @param destination destination object, null value must be allowed and can't cause exception
     */
    public abstract void configure(final S source, final D destination);

    void setMode(final MapMode mode) {
        this.mode = mode;
    }

    Class<S> getSourceClass() {
        return sourceClass;
    }

    Class<D> getDestinationClass() {
        return destinationClass;
    }

    protected Map<S, D> useConvention(final MappingConvention mappingConvention) {
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
    protected <T> Map<S, D> bind(
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
    protected <T> Map<S, D> bindConstant(
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

    protected <T> Map<S, D> bindByConvention(
            final BiConsumer<D, T> member,
            final MappingConvention convention,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> Map<S, D> setOption(
            final BiConsumer<D, T> member,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> Map<S, D> verifyAllDestinationPropertiesConfigured() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> beforeMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> afterMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> Map<S, D> beforeMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> Map<S, D> afterMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> beforeMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> afterMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> convertUsing(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> constructDestinationObjectUsing(final Supplier<D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Map<S, D> constructDestinationObjectUsing(final Function<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> Map<D, S> withReverseMap(final ReverseMapOption reverseMapOption) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
