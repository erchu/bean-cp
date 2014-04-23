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
import static org.beancp.Util.*;

class MapperSelector {

    private MapperSelector() {
        throw new IllegalStateException("Not allowed to create instance of this class");
    }

    public static boolean isMappingAvailable(final Class sourceClass,
            final Class destinationClass, final Collection<MappingExecutor<?, ?>> inCollection,
            final MappingConvention mapAnyConvention) {
        return (mapAnyConvention != null) || MapperSelector.getMappingExecutor(
                sourceClass, destinationClass,
                inCollection) != null;
    }

    public static MappingExecutor<?, ?> getMappingExecutor(
            final Class sourceClass, final Class destinationClass,
            final Collection<MappingExecutor<?, ?>> inCollection)
            throws MappingException {
        List<MappingExecutor<?, ?>> validMappers = inCollection.stream().filter(
                n -> canBeMapped(sourceClass, n.getSourceClass())
                && canBeMapped(destinationClass, n.getDestinationClass()))
                .collect(Collectors.toList());

        if (validMappers.isEmpty()) {
            return null;
        }

        MappingExecutor<?, ?> mapper
                = getBestMatchingMappingExecutor(sourceClass, destinationClass, validMappers);

        return mapper;
    }

    private static MappingExecutor<?, ?> getBestMatchingMappingExecutor(
            final Class sourceClass,
            final Class destinationClass,
            final List<MappingExecutor<?, ?>> validMappers) {
        return firstNotNull(
                firstOrNull(validMappers, (n
                        -> sourceClass.equals(n.getSourceClass())
                        && destinationClass.equals(n.getDestinationClass()))),
                firstOrNull(validMappers, (n
                        -> destinationClass.equals(n.getDestinationClass()))),
                firstOrNull(validMappers, (n
                        -> sourceClass.equals(n.getSourceClass()))),
                validMappers.get(0));
    }

    private static boolean canBeMapped(final Class objectClazz, final Class asMappingSideClass) {
        return asMappingSideClass.isAssignableFrom(objectClazz);
    }

    private static <T> T firstOrNull(Collection<T> collection, Predicate<T> filter) {
        Optional<T> findFirst = collection
                .stream()
                .filter(filter)
                .findFirst();

        return (findFirst.isPresent() ? findFirst.get() : null);
    }
}
