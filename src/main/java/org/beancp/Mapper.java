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

/**
 * Object to object mapper.
 *
 * @author Rafal Chojnacki
 */
public interface Mapper {

    /**
     * Copies data from source object to destination object.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param destination destination object.
     */
    <S, D> void map(S source, D destination);

    /**
     * Constructs destination object and copies data from source object to newly
     * created destination object.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param target destination object class.
     * @return destination object.
     */
    <S, D> D map(S source, Class<D> target);
}
