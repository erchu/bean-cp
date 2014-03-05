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

import com.rits.cloning.Cloner;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Rafal Chojnacki
 */
public class MapperBuilder {

    private final List<Map<?, ?>> maps = new LinkedList<>();

    private final Cloner cloner = new Cloner();

    public <S, D> MapperBuilder addMap(final Map<S, D> mapDefinition) {
        mapDefinition.configure(null, null);

        maps.add(mapDefinition);

        return this;
    }

    public Mapper buildMapper() {
        List<Map<?, ?>> mapsClone = cloner.deepClone(maps);
        mapsClone.stream().forEach(n -> n.setMode(MapMode.EXECUTION));

        return new MapperImpl(mapsClone);
    }
}
