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

/**
 * Builds mapper implementation.
 */
public class MapperBuilder {

    private final List<MapImpl<?, ?>> maps = new LinkedList<>();

    /**
     * Adds new map.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @param mapConfiguration map configuration.
     * @return this (for method chaining)
     */
    public <S, D> MapperBuilder addMap(final Class<S> sourceClass, final Class<D> destinationClass,
            final MapSetup<S, D> mapConfiguration) {
        MapImpl map = new MapImpl(sourceClass, destinationClass, mapConfiguration);
        map.configure();

        maps.add(map);

        return this;
    }

    /**
     * Creates map implementation from definitions.
     *
     * @return map implementation.
     */
    public Mapper buildMapper() {
        return new MapperImpl(maps);
    }
}
