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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class MapperImpl implements Mapper {

    private final List<MapImpl<?, ?>> maps;

    MapperImpl(final List<MapImpl<?, ?>> maps) {
        this.maps = maps;
    }

    private MapImpl<?, ?> getMapper(final Class sourceClass, final Class destinationClass)
            throws MappingException {
        List<MapImpl<?, ?>> validMappers = maps.stream().filter(
                n -> canBeMapped(sourceClass, n.getSourceClass())
                && canBeMapped(destinationClass, n.getDestinationClass()))
                .collect(Collectors.toList());

        if (validMappers.isEmpty()) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.",
                            sourceClass, destinationClass));
        }

        MapImpl<?, ?> mapper = getBestMatchingMapper(sourceClass, destinationClass, validMappers);

        return mapper;
    }

    @Override
    public <S, D> void map(S source, D destination) {
        if (source == null) {
            throw new NullParameterException("source");
        }

        if (destination == null) {
            throw new NullParameterException("destination");
        }

        MapImpl<S, D> mapper = (MapImpl<S, D>) getMapper(source.getClass(), destination.getClass());

        mapper.execute(this, source, destination);
    }

    @Override
    public <S, D> D map(final S source, final Class<D> destinationClass) {
        if (source == null) {
            throw new NullParameterException("source");
        }

        if (destinationClass == null) {
            throw new NullParameterException("destinationClass");
        }

        MapImpl<S, D> mapper = (MapImpl<S, D>) getMapper(source.getClass(), destinationClass);

        return mapper.execute(this, source, destinationClass);
    }

    private MapImpl<?, ?> getBestMatchingMapper(
            final Class sourceClass,
            final Class destinationClass,
            final List<MapImpl<?, ?>> validMappers) {
        return coalesce(
                firstOrNull(validMappers, (n
                        -> sourceClass.equals(n.getSourceClass())
                        && destinationClass.equals(n.getDestinationClass()))),
                firstOrNull(validMappers, (n
                        -> destinationClass.equals(n.getDestinationClass()))),
                firstOrNull(validMappers, (n
                        -> sourceClass.equals(n.getSourceClass()))),
                validMappers.get(0));
    }

    private <T> T firstOrNull(Collection<T> collection, Predicate<T> filter) {
        Optional<T> findFirst = collection
                .stream()
                .filter(filter)
                .findFirst();

        return (findFirst.isPresent() ? findFirst.get() : null);
    }

    private <T> T coalesce(T... args) {
        for (T i : args) {
            if (i != null) {
                return i;
            }
        }

        return null;
    }

    private boolean canBeMapped(final Class objectClazz, final Class asMappingSideClass) {
        return asMappingSideClass.isAssignableFrom(objectClazz);
    }
}
