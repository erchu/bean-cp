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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.commons.lang3.ClassUtils;
import static org.apache.commons.lang3.Validate.*;

class MapperImpl implements Mapper {

    private final Collection<MapImpl<?, ?>> _maps;

    private final Collection<Converter<?, ?>> _converters;

    private final List<MapConventionExecutor> _mapAnyConventions;

    MapperImpl(
            final Collection<Converter<?, ?>> converters,
            final List<MapImpl<?, ?>> maps,
            final List<MapConventionExecutor> mapAnyConvention) {
        this._converters = Collections.unmodifiableCollection(converters);
        this._maps = Collections.unmodifiableCollection(maps);
        this._mapAnyConventions = mapAnyConvention;
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
        notNull(source, "source");
        notNull(destination, "destination");

        try (MappingContext context = new MappingContext()) {
            MapImpl<S, D> map = (MapImpl<S, D>) MapperExecutorSelector.getBestMatchingMap(
                    source.getClass(), destination.getClass(),
                    _maps);

            // We intentionally don't put result in context, because destination was object created
            // outside of the mapper and we don't want reuse it.
            return mapIfMapperAvailable(map, source, destination);
        } catch (Exception ex) {
            throw new MappingException(
                    String.format("Failed to map %s to %s.", source, destination), ex);
        }
    }

    @Override
    public <S, D> D map(final S source, final Class<D> destinationClass) {
        notNull(source, "source");
        notNull(destinationClass, "destinationClass");

        Optional<D> result = mapIfMapperAvailable(source, destinationClass);

        if (result.isPresent() == false) {
            throw new MappingException(
                    String.format("No suitable converter or map found to map from %s to %s.",
                            source.getClass(), destinationClass));
        } else {
            return result.get();
        }
    }

    @Override
    @SuppressWarnings("TooBroadCatch")
    public <S, D> Optional<D> mapIfMapperAvailable(
            final S source, final Class<D> destinationClass) throws MappingException {
        notNull(source, "source");
        notNull(destinationClass, "destinationClass");

        Class sourceClass = source.getClass();

        try (MappingContext context = new MappingContext()) {
            Optional<Object> alreadyAvailableResult
                    = context.getMappingResult(source, destinationClass);

            if (alreadyAvailableResult.isPresent()) {
                return Optional.of((D) alreadyAvailableResult.get());
            }

            Converter<S, D> converter
                    = (Converter<S, D>) MapperExecutorSelector.getBestMatchingConverter(
                            sourceClass, destinationClass, _converters);

            if (converter != null) {
                if (sourceClass.isArray() && sourceClass.getComponentType().isPrimitive()) {
                    Object[] sourceWrapper = getArrayOfPrimitiveTypeWrapper(sourceClass, source);
                    D destination
                            = (D) ((Converter<Object, D>) converter).convert(this, sourceWrapper);

                    context.addMappingResult(source, destination, destinationClass);

                    return Optional.of(destination);
                } else {
                    D destination = (D) converter.convert(this, source);

                    context.addMappingResult(source, destination, destinationClass);

                    return Optional.of(destination);
                }
            }

            MapImpl<S, D> map = (MapImpl<S, D>) MapperExecutorSelector.getBestMatchingMap(
                    sourceClass, destinationClass, _maps);

            D destination = null;

            if (map != null && map.getDestinationObjectBuilder() != null) {
                destination = constructObjectUsingDestinationObjectBuilder(
                        map.getDestinationObjectBuilder(), destinationClass);
            }

            // if MapImpl is not available or has no specific destination object builder
            if (destination == null) {
                destination = constructObjectUsingDefaultConstructor(destinationClass);
                context.addMappingResult(source, destination, destinationClass);
            }

            if (mapIfMapperAvailable(source, destination)) {
                return Optional.of(destination);
            } else {
                context.removeMappingResult(source, destinationClass);

                return Optional.empty();
            }
        } catch (Exception ex) {
            throw new MappingException(
                    String.format(
                            "Failed to map from %s to %s class",
                            sourceClass, destinationClass),
                    ex);
        }
    }

    private <S> Object[] getArrayOfPrimitiveTypeWrapper(final Class sourceClass, final S source)
            throws
            IllegalArgumentException,
            NegativeArraySizeException,
            ArrayIndexOutOfBoundsException {
        Class<?> arrayElementWrapperClass
                = ClassUtils.primitiveToWrapper(sourceClass.getComponentType());
        int arrayLength = Array.getLength(source);
        Object[] sourceWrapper = (Object[]) Array.newInstance(
                arrayElementWrapperClass, arrayLength);
        for (int i = 0 ; i < arrayLength ; i++) {
            sourceWrapper[i] = Array.get(source, i);
        }
        return sourceWrapper;
    }

    @Override
    public boolean isMapAvailable(final Class sourceClass, final Class destinationClass) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        return MapperExecutorSelector.isMapAvailable(
                this,
                sourceClass,
                destinationClass,
                _maps,
                _mapAnyConventions);
    }

    @Override
    public boolean isConverterAvailable(final Class sourceClass, final Class destinationClass) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        return MapperExecutorSelector.isConverterAvailable(
                this,
                sourceClass,
                destinationClass,
                Collections.unmodifiableCollection(_converters));
    }

    private <D, S> boolean mapIfMapperAvailable(
            final MapImpl<S, D> MapImpl, final S source, final D destination) {
        if (MapImpl != null) {
            MapImpl.execute(this, source, destination);

            return true;
        }

        for (MapConventionExecutor i : _mapAnyConventions) {
            if (i.tryMap(this, source, destination)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings({ "TooBroadCatch", "UseSpecificCatch" })
    private <D> D constructObjectUsingDefaultConstructor(
            final Class<D> destinationClass) throws MappingException {
        try {
            if (destinationClass.isPrimitive()) {
                return (D) ClassUtils.primitiveToWrapper(destinationClass);
            } else {
                return (D) destinationClass.newInstance();
            }
        } catch (Exception ex) {
            throw new MappingException("Cannot create destination instance.", ex);
        }
    }

    private <D> D constructObjectUsingDestinationObjectBuilder(
            final Supplier<D> destinationObjectBuilder,
            final Class<D> destinationClass) throws MappingException {
        notNull(destinationObjectBuilder, "destinationObjectBuilder");
        notNull(destinationClass, "destinationClass");

        D destination;

        try {
            destination = destinationObjectBuilder.get();
        } catch (Exception ex) {
            throw new MappingException(String.format("Failed to create destination object class %s "
                    + "using constructDestinationObjectUsing.", destinationClass));
        }

        if (destinationClass.isAssignableFrom(destination.getClass()) == false) {
            throw new MappingException(String.format("Destination object class %s returned "
                    + "by constructDestinationObjectUsing cannot be assigned to expected "
                    + "class %s.", destination.getClass(), destinationClass));
        }

        return destination;
    }
}
