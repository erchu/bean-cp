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

    private final List<Map<?, ?>> mapDefinitions;

    MapperImpl(final List<Map<?, ?>> mapDefinitions) {
        //TODO: Temporary solution
        this.mapDefinitions = mapDefinitions;
    }

    @Override
    public <S, D> void map(S source, D destination) {
        Class sourceClass = source.getClass();
        Class destinationClass = destination.getClass();

        //TODO: Temporary solution, too simple ;-) solution
        Optional<Map<?, ?>> mapperHolder = mapDefinitions.stream().filter(
                n
                -> n.getSourceClass().equals(sourceClass)
                && n.getDestinationClass().equals(destinationClass))
                .findFirst();

        if (!mapperHolder.isPresent()) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.", source, destination));
        }

        Map<S, D> mapper = (Map<S, D>) mapperHolder.get();
        mapper.configure(source, destination);
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
}
