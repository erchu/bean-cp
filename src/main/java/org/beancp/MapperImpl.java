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
import static org.beancp.NullParameterException.failIfNull;

class MapperImpl implements Mapper {

    private final Collection<MappingExecutor<?, ?>> mappingExecutors;

    private MappingConvention mapAnyConvention;

    MapperImpl(final List<MappingExecutor<?, ?>> mappingExecutors,
            final MappingConvention mapAnyConvention) {
        this.mappingExecutors = Collections.unmodifiableCollection(mappingExecutors);
        this.mapAnyConvention = mapAnyConvention;
    }

    @Override
    public <S, D> void map(S source, D destination) {
        failIfNull(source, "source");
        failIfNull(destination, "destination");

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
        failIfNull(source, "source");
        failIfNull(destinationClass, "v");

        Class sourceClass = source.getClass();

        MappingExecutor<S, D> mappingExecutor
                = (MappingExecutor<S, D>) MapperSelector.getMappingExecutor(
                        sourceClass, destinationClass,
                        mappingExecutors);

        if (mappingExecutor == null) {
            if (mapAnyConvention != null) {
                D destination = constructDestinationObject(destinationClass);
                mapAnyConvention.execute(this, source, destination);
                
                return destination;
            } else {
                throw new MappingException(
                        String.format("No suitable mapping found from %s to %s.",
                                sourceClass, destinationClass));
            }
        }

        return mappingExecutor.execute(this, source, destinationClass);
    }

    @Override
    public boolean isAvailable(final Class sourceClass, final Class destinationClass) {
        return MapperSelector.isMappingAvailable(
                sourceClass, destinationClass,
                mappingExecutors, mapAnyConvention);
    }

    private <D> D constructDestinationObject(final Class<D> destinationClass) throws MappingException {
        try {
            return (D) destinationClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException("Cannot create destination instance.", ex);
        }
    }
}
