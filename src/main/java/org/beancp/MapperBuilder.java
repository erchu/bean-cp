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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import static org.apache.commons.lang3.Validate.*;

/**
 * Builds mapper implementation. This class do not guarantee to be thread-safe.
 */
public final class MapperBuilder implements MappingInfo {

    private final List<MapImpl<?, ?>> _maps = new LinkedList<>();

    private final List<Converter<?, ?>> _converters = new LinkedList<>();

    private final List<MapConventionExecutor> _mapAnyConventions = new LinkedList<>();

    private boolean _mapperBuilded = false;

    /**
     * Adds new mapping defined by map. Both {@code source} and {@code destination} classes must:
     * <ul>
     * <li>Must have default public constructor or have not been final and have default protected
     * constructor. This requirement is valid even if destination object builder is provided by
     * {@link Map#constructDestinationObjectUsing(java.util.function.Supplier)} method.</li>
     * <li>Cannot be inner non-static classes.</li>
     * </ul>
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
            final MapSetup<S, D> mapConfiguration) throws MapperConfigurationException {
        validateAddMappingAction(sourceClass, destinationClass);

        MapImpl map = new MapImpl(sourceClass, destinationClass, mapConfiguration);
        map.configure(this);

        _maps.add(map);

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(
            final Class<S> sourceClass,
            final Class<D> destinationClass,
            final Function<S, D> convertionAction)
            throws MapperConfigurationException {
        validateAddMappingAction(sourceClass, destinationClass);

        _converters.add(new Converter<>(sourceClass, destinationClass, convertionAction));

        return this;
    }

    /**
     * Adds new mapping implemented by converter.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param convertionAction converter action, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addConverter(
            final Class<S> sourceClass,
            final Class<D> destinationClass,
            final BiFunction<Mapper, S, D> convertionAction) throws MapperConfigurationException {
        validateAddMappingAction(sourceClass, destinationClass);

        _converters.add(new Converter<>(sourceClass, destinationClass, convertionAction));

        return this;
    }

    /**
     * Adds new mapping implemented by converters.
     *
     * @param converters converters to add.
     *
     * @return this (for method chaining)
     */
    public MapperBuilder addConverter(
            final Converter<?, ?>... converters) throws MapperConfigurationException {

        for (Converter<?, ?> i : converters) {
            validateAddMappingAction(i.getSourceClass(), i.getDestinationClass());
        }

        this._converters.addAll(Arrays.asList(converters));

        return this;
    }

    /**
     * If two data types has no mapping defined by
     * {@link #addMap(java.lang.Class, java.lang.Class, org.beancp.MapSetup)} or any of
     * {@code addConverter} methods then this convention will be used.
     *
     * @param conventions convention to add.
     *
     * @return this (for method chaining)
     */
    public MapperBuilder addMapAnyByConvention(final MapConvention... conventions)
            throws MapperConfigurationException {
        List<MapConventionExecutor> conventionExecutors = Arrays.stream(conventions)
                .map(i -> new MapConventionExecutor(i))
                .collect(Collectors.toList());

        this._mapAnyConventions.addAll(conventionExecutors);

        return this;
    }

    /**
     * Creates map implementation from definitions. After executed no other methods can be executed
     * on this instance.
     *
     * @return map implementation.
     */
    public Mapper buildMapper() {
        this._mapperBuilded = true;

        return new MapperImpl(_converters, _maps, _mapAnyConventions);
    }

    @Override
    public boolean isMapAvailable(final Class sourceClass, final Class destinationClass) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        return MapperExecutorSelector.isMapAvailable(
                this,
                sourceClass,
                destinationClass,
                Collections.unmodifiableCollection(_maps),
                Collections.unmodifiableCollection(_mapAnyConventions));
    }

    @Override
    public boolean isConverterAvailable(final Class sourceClass, final Class destinationClass) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        return MapperExecutorSelector.isConverterAvailable(
                this,
                sourceClass,
                destinationClass,
                Collections.unmodifiableCollection(_converters));
    }

    private <S, D> void validateAddMappingAction(final Class<S> sourceClass,
            final Class<D> destinationClass) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        if (this._mapperBuilded) {
            throw new MapperConfigurationException("Mapper already builded. No changes allowed.");
        }

        for (MapImpl<?, ?> i : _maps) {
            if (i.getSourceClass().equals(sourceClass)
                    && i.getDestinationClass().equals(destinationClass)) {
                throw new MapperConfigurationException(String.format(
                        "Mapping from %s to %s already defined.",
                        sourceClass.getName(), destinationClass.getName()));
            }
        }
    }
}
