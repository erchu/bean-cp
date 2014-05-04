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
import org.apache.commons.lang3.ClassUtils;
import static org.apache.commons.lang3.ObjectUtils.*;
import static org.apache.commons.lang3.Validate.*;

class MapperExecutorSelector {

    static enum MapperExecutorMatchMode {

        ANY,
        STRICT_DESTINATION
    }

    private MapperExecutorSelector() {
        throw new IllegalStateException("Not allowed to create instance of this class");
    }

    public static boolean isMapAvailable(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass,
            final Collection<MapImpl<?, ?>> maps,
            final Collection<MapConvention> mapAnyConventions) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");
        notNull(maps, "maps");

        if (getBestMatchingMap(sourceClass, destinationClass, maps) != null) {
            return true;
        }

        return mapAnyConventions.stream()
                .anyMatch(i -> i.canMap(mappingsInfo, sourceClass, destinationClass));
    }

    static boolean isConverterAvailable(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass,
            final Collection<Converter<?, ?>> converters) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");
        notNull(converters, "converters");

        return (getBestMatchingConverter(sourceClass, destinationClass, converters) != null);
    }

    public static Converter<?, ?> getBestMatchingConverter(
            final Class sourceClass,
            final Class destinationClass,
            final Collection<Converter<?, ?>> executors) {
        return (Converter<?, ?>) getBestMatchingMappingExecutor(
                sourceClass,
                destinationClass,
                executors,
                MapperExecutorMatchMode.STRICT_DESTINATION);
    }

    public static MapImpl<?, ?> getBestMatchingMap(
            final Class sourceClass,
            final Class destinationClass,
            final Collection<MapImpl<?, ?>> executors) {
        return (MapImpl<?, ?>) getBestMatchingMappingExecutor(
                sourceClass,
                destinationClass,
                executors,
                MapperExecutorMatchMode.ANY);
    }

    private static <T extends MappingExecutor<?, ?>> T getBestMatchingMappingExecutor(
            final Class sourceClass,
            final Class destinationClass,
            final Collection<T> executors,
            final MapperExecutorMatchMode matchMode) {
        List<T> validMappers = executors.stream().filter(
                i -> canBeMapped(sourceClass, i.getSourceClass())
                && canBeMapped(destinationClass, i.getDestinationClass()))
                .collect(Collectors.toList());

        if (validMappers.isEmpty()) {
            return null;
        }

        T mapper;

        if (matchMode == MapperExecutorMatchMode.STRICT_DESTINATION) {
            mapper = firstNonNull(
                    firstOrNull(validMappers,
                            (i -> classEqualsOrWrapper(sourceClass, i.getSourceClass())
                            && classEqualsOrWrapper(destinationClass, i.getDestinationClass()))),
                    firstOrNull(validMappers,
                            (i -> classEqualsOrWrapper(destinationClass, i.getDestinationClass()))));
        } else {
            mapper = firstNonNull(
                    firstOrNull(validMappers,
                            (i -> classEqualsOrWrapper(sourceClass, i.getSourceClass())
                            && classEqualsOrWrapper(destinationClass, i.getDestinationClass()))),
                    firstOrNull(validMappers,
                            (i -> classEqualsOrWrapper(destinationClass, i.getDestinationClass()))),
                    firstOrNull(validMappers,
                            (i -> classEqualsOrWrapper(sourceClass, i.getSourceClass()))),
                    validMappers.get(0));
        }

        return mapper;
    }

    /**
     * Returns first value in {@code collection} which is not equal to null and matches filter. If
     * there is not such element then will return null.
     *
     * @param <T> result type.
     * @param collection collection to search in.
     * @param filter filter predicate.
     * @return first value in {@code collection} which is not equal to null and matches filter, if
     * there is not such element then will return null.
     */
    private static <T> T firstOrNull(final Collection<T> collection, final Predicate<T> filter) {
        Optional<T> findFirst = collection
                .stream()
                .filter(filter)
                .findFirst();

        return (findFirst.isPresent() ? findFirst.get() : null);
    }

    private static boolean canBeMapped(final Class objectClass, final Class supportedClass) {
        return classEqualsOrWrapper(objectClass, supportedClass)
                || supportedClass.isAssignableFrom(objectClass);
    }

    private static boolean classEqualsOrWrapper(
            final Class objectClass, final Class supportedClass) {
        if (objectClass.isPrimitive()) {
            Class<?> primitiveTypeWrapper = ClassUtils.primitiveToWrapper(objectClass);

            return objectClass.equals(supportedClass)
                    || primitiveTypeWrapper.equals(supportedClass);
        } else if (supportedClass.isPrimitive()) {
            Class<?> primitiveTypeWrapper = ClassUtils.primitiveToWrapper(supportedClass);

            return objectClass.equals(supportedClass)
                    || objectClass.equals(primitiveTypeWrapper);
        } else {
            return supportedClass.equals(objectClass);
        }
    }
}
