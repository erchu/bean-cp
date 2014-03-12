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

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Rafal Chojnacki
 */
class MapperImpl implements Mapper {

    private final List<MapImpl<?, ?>> maps;

    MapperImpl(final List<MapImpl<?, ?>> maps) {
        this.maps = maps;
    }

    @Override
    public <S, D> void map(S source, D destination) {
        if (source == null) {
            throw new NullParameterException("source");
        }

        if (destination == null) {
            throw new NullParameterException("destination");
        }

        Class sourceClass = source.getClass();
        Class destinationClass = destination.getClass();

        Optional<MapImpl<?, ?>> mapperHolder = maps.stream().filter(
                n
                -> canBeMapped(sourceClass, n.getSourceClass())
                && canBeMapped(destinationClass, n.getDestinationClass()))
                .findFirst();

        if (!mapperHolder.isPresent()) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.", source, destination));
        }

        MapImpl<S, D> mapper = (MapImpl<S, D>) mapperHolder.get();
        mapper.execute(source, destination);
    }

    @Override
    public <S, D> D map(S source, Class<D> target) {
        //TODO: Error handling

        try {
            //TODO: constructUsing support
            D destination = (D) target.newInstance();

            map(source, destination);

            return destination;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException("Cannot create destination instance.", ex);
        }
    }

    private boolean canBeMapped(final Class objectClazz, final Class asMappingSideClass) {
        //TODO: options and subtypes handling

        return objectClazz.equals(asMappingSideClass);
    }
}
