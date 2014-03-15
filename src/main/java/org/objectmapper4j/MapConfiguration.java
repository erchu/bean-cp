/*
 * ObjectMapper4j
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
package org.objectmapper4j;

/**
 *
 * @author Rafal Chojnacki
 * @param <S> mapping source
 * @param <D> mapping destination
 */
@FunctionalInterface
public interface MapConfiguration<S, D> {

    /**
     * Defines map configuration. Implementation must be thread safe and has no side effects other
     * that binding definition. Method could be called more than once.
     *
     * @param config configuration
     * @param ref source and destination objects holder
     */
    void apply(final Map<S, D> config, final MapObjectsReference<S, D> ref);
}
