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

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Builds mapper implementation.
 */
public final class MapperBuilder {

    private final List<MappingExecutor<?, ?>> mappingExecutors = new LinkedList<>();

    //TODO: Document constraints
    /**
     * Adds new mapping defined by map.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param mapConfiguration map configuration.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addMap(final Class<S> sourceClass, final Class<D> destinationClass,
            final MapSetup<S, D> mapConfiguration) {
        //TODO: validate if mapping already defined
        MapImpl map = new MapImpl(sourceClass, destinationClass, mapConfiguration);
        map.configure();

        mappingExecutors.add(map);

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(final Class<S> sourceClass,
            final Class<D> destinationClass,
            final BiConsumer<S, D> convertionAction) {
        TriConsumer<Mapper, S, D> convertActionWrapper
                = (Mapper mapper, S source, D destination)
                -> convertionAction.accept(source, destination);

        addConverter(sourceClass, destinationClass, convertActionWrapper);

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action.
     * @param destinationObjectBuilder destination object builder.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(final Class<S> sourceClass,
            final Class<D> destinationClass,
            final BiConsumer<S, D> convertionAction,
            final Supplier<D> destinationObjectBuilder) {
        TriConsumer<Mapper, S, D> convertActionWrapper
                = (Mapper mapper, S source, D destination)
                -> convertionAction.accept(source, destination);

        addConverter(sourceClass, destinationClass, convertActionWrapper, destinationObjectBuilder);

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(final Class<S> sourceClass,
            final Class<D> destinationClass,
            final TriConsumer<Mapper, S, D> convertionAction) {
        addConverter(sourceClass, destinationClass, convertionAction, null);

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action.
     * @param destinationObjectBuilder destination object builder.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(final Class<S> sourceClass,
            final Class<D> destinationClass,
            final TriConsumer<Mapper, S, D> convertionAction,
            final Supplier<D> destinationObjectBuilder) {
        //TODO: validate if mapping already defined
        Converter converter = new Converter(
                sourceClass, destinationClass, convertionAction, destinationObjectBuilder);

        mappingExecutors.add(converter);

        return this;
    }

    /**
     * Creates map implementation from definitions.
     *
     * @return map implementation.
     */
    public Mapper buildMapper() {
        return new MapperImpl(mappingExecutors);
    }
}
