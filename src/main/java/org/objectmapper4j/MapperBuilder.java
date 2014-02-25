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

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Rafal Chojnacki
 */
public class MapperBuilder {

    private List<MapDefinition<?, ?>> mapDefinitions = new LinkedList<>();

    public <S, D> MapDefinition<S, D> addMap(final Class<S> source, final Class<D> destination) {
        if (source == null) {
            throw new NullPointerException("Null not allowed for 'source' parameter.");
        }

        if (destination == null) {
            throw new NullPointerException("Null not allowed for 'destination' parameter.");
        }

        //TODO: Temporary solution
        MapDefinition<S, D> newMapDefinition = new MapDefinition<>(source, destination);
        mapDefinitions.add(newMapDefinition);

        return newMapDefinition;
    }

    public Mapper buildMapper() {
        //TODO: Temporary solution
        return new MapperImpl(mapDefinitions);
    }
}
