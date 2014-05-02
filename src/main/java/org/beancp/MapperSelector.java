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
import static org.beancp.CollectionUtils.*;
import static org.beancp.ConstraintUtils.*;

class MapperSelector {

    private MapperSelector() {
        throw new IllegalStateException("Not allowed to create instance of this class");
    }

    public static boolean isMappingAvailable(
            final MappingsInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass,
            final Collection<MapExecutor<?, ?>> mapExecutors,
            final Collection<MappingConvention> mapAnyConventions) {
        failIfNull(sourceClass, "sourceClass");
        failIfNull(destinationClass, "destinationClass");
        failIfNull(mapExecutors, "inCollection");

        if (getBestMatchingMapExecutor(sourceClass, destinationClass, mapExecutors) != null) {
            return true;
        }

        return mapAnyConventions.stream()
                .anyMatch(i -> i.canMap(mappingsInfo, sourceClass, destinationClass));
    }

    public static MapExecutor<?, ?> getBestMatchingMapExecutor(
            final Class sourceClass,
            final Class destinationClass,
            final Collection<MapExecutor<?, ?>> mapExecutors)
            throws MappingException {
        List<MapExecutor<?, ?>> validMappers = mapExecutors.stream().filter(
                i -> canBeMapped(sourceClass, i.getSourceClass())
                && canBeMapped(destinationClass, i.getDestinationClass()))
                .collect(Collectors.toList());

        if (validMappers.isEmpty()) {
            return null;
        }

        MapExecutor<?, ?> mapper
                = firstNotNullOrNull(
                        firstOrNull(validMappers,
                                (i -> sourceClass.equals(i.getSourceClass())
                                && destinationClass.equals(i.getDestinationClass()))),
                        firstOrNull(validMappers,
                                (i -> destinationClass.equals(i.getDestinationClass()))),
                        firstOrNull(validMappers,
                                (i -> sourceClass.equals(i.getSourceClass()))),
                        validMappers.get(0));

        return mapper;
    }

    private static boolean canBeMapped(final Class objectClazz, final Class asMappingSideClass) {
        return asMappingSideClass.isAssignableFrom(objectClazz);
    }
}
