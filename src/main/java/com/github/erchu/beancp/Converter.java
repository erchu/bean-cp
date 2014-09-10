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
package com.github.erchu.beancp;

import java.util.function.BiFunction;
import java.util.function.Function;
import static org.apache.commons.lang3.Validate.*;

/**
 * Converter used to convert source to usually immutable new destination object.
 *
 * @param <S> conversion source class.
 * @param <D> conversion destination class.
 */
public final class Converter<S, D> implements MappingExecutor<S, D> {

    private final Class<S> _sourceClass;

    private final Class<D> _destinationClass;

    private final BiFunction<Mapper, S, D> _convertionAction;

    /**
     * Creates converter instance.
     *
     * @param sourceClass source class.
     * @param destinationClass destination class.
     * @param convertAction conversion action.
     */
    public Converter(
            final Class<S> sourceClass,
            final Class<D> destinationClass,
            final Function<S, D> convertAction) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");
        notNull(convertAction, "convertAction");

        BiFunction<Mapper, S, D> convertActionWrapper
                = (Mapper mapper, S source) -> convertAction.apply(source);

        this._sourceClass = sourceClass;
        this._destinationClass = destinationClass;
        this._convertionAction = convertActionWrapper;
    }

    /**
     * Creates converter instance.
     *
     * @param sourceClass source class.
     * @param destinationClass destination class.
     * @param convertAction conversion action.
     */
    public Converter(
            final Class<S> sourceClass,
            final Class<D> destinationClass,
            final BiFunction<Mapper, S, D> convertAction) {
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");
        notNull(convertAction, "convertAction");

        this._sourceClass = sourceClass;
        this._destinationClass = destinationClass;
        this._convertionAction = convertAction;
    }

    /**
     * Returns source class supported by this converter.
     *
     * @return source class supported by this converter.
     */
    @Override
    public Class<S> getSourceClass() {
        return _sourceClass;
    }

    /**
     * Returns destination class supported by this converter.
     *
     * @return destination class supported by this converter.
     */
    @Override
    public Class<D> getDestinationClass() {
        return _destinationClass;
    }

    /**
     * Performs conversion. Must be thread-safe.
     *
     * @param caller mapper requesting this operation.
     * @param source source object.
     * @return conversion result.
     */
    public D convert(final Mapper caller, final S source) {
        return _convertionAction.apply(caller, source);
    }
}
