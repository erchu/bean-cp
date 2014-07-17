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
 * Returns information of configured mappings.
 */
public interface MappingInfo {

    /**
     * Returns {@code true} if converter is available, {@code false} otherwise.
     *
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     *
     * @return {@code true} if converter is available, {@code false} otherwise.
     */
    boolean isConverterAvailable(Class sourceClass, Class destinationClass);

    /**
     * Returns {@code true} if map (or map convention) is available, {@code false} otherwise.
     *
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     *
     * @return {@code true} if map (or map convention) is available, {@code false} otherwise.
     */
    boolean isMapAvailable(Class sourceClass, Class destinationClass);
}
