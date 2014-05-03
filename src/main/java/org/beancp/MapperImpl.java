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
import java.util.Optional;
import java.util.function.Supplier;
import static org.beancp.ConstraintUtils.failIfNull;

class MapperImpl implements Mapper {

    private final Collection<MapExecutor<?, ?>> mapExecutors;

    private final List<MappingConvention> mapAnyConventions;

    MapperImpl(final List<MapExecutor<?, ?>> mapExecutors,
            final List<MappingConvention> mapAnyConvention) {
        this.mapExecutors = Collections.unmodifiableCollection(mapExecutors);
        this.mapAnyConventions = mapAnyConvention;
    }

    @Override
    public <S, D> void map(final S source, final D destination) throws MappingException {
        if (mapIfMapperAvailable(source, destination) == false) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.",
                            source.getClass(), destination.getClass()));
        }
    }

    @Override
    public <S, D> boolean mapIfMapperAvailable(
            final S source, final D destination) throws MappingException {
        failIfNull(source, "source");
        failIfNull(destination, "destination");

        MapExecutor<S, D> mapExecutor
                = (MapExecutor<S, D>) MapperSelector.getBestMatchingMapExecutor(
                        source.getClass(), destination.getClass(),
                        mapExecutors);

        return mapIfMapperAvailable(mapExecutor, source, destination);
    }

    @Override
    public <S, D> D map(final S source, final Class<D> destinationClass) {
        failIfNull(source, "source");
        failIfNull(destinationClass, "destinationClass");

        Optional<D> result = mapIfMapperAvailable(source, destinationClass);

        if (result.isPresent() == false) {
            throw new MappingException(
                    String.format("No suitable mapping found from %s to %s.",
                            source.getClass(), destinationClass));
        } else {
            return result.get();
        }
    }

    @Override
    public <S, D> Optional<D> mapIfMapperAvailable(
            final S source, final Class<D> destinationClass) throws MappingException {
        failIfNull(source, "source");
        failIfNull(destinationClass, "destinationClass");

        try {
            MapExecutor<S, D> mapExecutor
                    = (MapExecutor<S, D>) MapperSelector.getBestMatchingMapExecutor(
                            source.getClass(), destinationClass,
                            mapExecutors);

            D destination = null;

            if (mapExecutor != null && mapExecutor.getDestinationObjectBuilder() != null) {
                destination = constructObjectUsingDestinationObjectBuilder(
                        mapExecutor.getDestinationObjectBuilder(), destinationClass);
            }

            // if mapExecutor is not available or has no specific destination object builder
            if (destination == null) {
                destination = constructObjectUsingDefaultConstructor(destinationClass);
            }

            if (mapIfMapperAvailable(source, destination)) {
                return Optional.of(destination);
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            throw new MappingException(
                    String.format(
                            "Failed to map from %s to %s",
                            source.getClass(), destinationClass),
                    ex);
        }
    }

    @Override
    public boolean isMapperAvailable(final Class sourceClass, final Class destinationClass) {
        return MapperSelector.isMappingAvailable(
                this,
                sourceClass,
                destinationClass,
                mapExecutors,
                mapAnyConventions);
    }

    private <D, S> boolean mapIfMapperAvailable(
            final MapExecutor<S, D> mapExecutor, final S source, final D destination) {
        if (mapExecutor != null) {
            mapExecutor.execute(this, source, destination);

            return true;
        }

        for (MappingConvention i : mapAnyConventions) {
            if (i.tryMap(this, source, destination)) {
                return true;
            }
        }

        return false;
    }

    private <D> D constructObjectUsingDefaultConstructor(
            final Class<D> destinationClass) throws MappingException {
        try {
            return (D) destinationClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException("Cannot create destination instance.", ex);
        }
    }

    private <D> D constructObjectUsingDestinationObjectBuilder(
            final Supplier<D> destinationObjectBuilder,
            final Class<D> destinationClass) throws MappingException {
        failIfNull(destinationObjectBuilder, "destinationObjectBuilder");
        failIfNull(destinationClass, "destinationClass");

        D destination = destinationObjectBuilder.get();

        //TODO: Use default constructor in this case
        if (destinationClass.isAssignableFrom(destination.getClass()) == false) {
            throw new MappingException(String.format("Destination object class %s returned "
                    + "by constructDestinationObjectUsing cannot be assigned to expected "
                    + "class %s.", destination.getClass(), destinationClass));
        }

        return destination;
    }
}
