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

/**
 * Interface for lambda expression used to define {@link DeclarativeMap}.
 *
 * Example shows how to use this interface, to bind <code>getX()</code> to <code>setA()</code>:
 *
 * <pre>
 * {@code
 * (config, source, destination) -> config.bind(source::getX, destination::setA))
 * }
 * </pre>
 *
 * @param <S> mapping source
 * @param <D> mapping destination
 *
 * @see MapperBuilder#addMap(java.lang.Class, java.lang.Class, com.github.erchu.beancp.MapSetup)
 */
@FunctionalInterface
public interface DeclarativeMapSetup<S, D> {

    /**
     * Defines new map configuration. Implementation must be thread safe and has no side effects
     * other that binding definition. Method could be called more than once.
     *
     * @param config configuration
     * @param source source and destination object
     * @param destination destination and destination object
     */
    void apply(final DeclarativeMap<S, D> config, final S source, final D destination);
}
