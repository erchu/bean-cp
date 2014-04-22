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
import java.util.Collections;
import java.util.List;

class MapperImpl implements Mapper {

    private final Collection<MappingExecutor<?, ?>> mappingExecutors;

    MapperImpl(final List<MappingExecutor<?, ?>> mappingExecutors) {
        this.mappingExecutors = Collections.unmodifiableCollection(mappingExecutors);
    }

    @Override
    public <S, D> void map(S source, D destination) {
        if (source == null) {
            throw new NullParameterException("source");
        }

        if (destination == null) {
            throw new NullParameterException("destination");
        }

        MappingExecutor<S, D> mappingExecutor
                = (MappingExecutor<S, D>) MapperSelector.getMappingExecutor(
                        source.getClass(), destination.getClass(),
                        mappingExecutors);

        if (mappingExecutor == null) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.",
                            source.getClass(), destination.getClass()));
        }

        mappingExecutor.execute(this, source, destination);
    }

    @Override
    public <S, D> D map(final S source, final Class<D> destinationClass) {
        if (source == null) {
            throw new NullParameterException("source");
        }

        if (destinationClass == null) {
            throw new NullParameterException("destinationClass");
        }

        Class sourceClass = source.getClass();

        MappingExecutor<S, D> mappingExecutor
                = (MappingExecutor<S, D>) MapperSelector.getMappingExecutor(
                        sourceClass, destinationClass,
                        mappingExecutors);

        if (mappingExecutor == null) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.",
                            sourceClass, destinationClass));
        }

        return mappingExecutor.execute(this, source, destinationClass);
    }

    @Override
    public boolean isAvailable(final Class sourceClass, final Class destinationClass) {
        return MapperSelector.mappingExecutorIsAvailable(
                sourceClass, destinationClass,
                mappingExecutors);
    }
}
