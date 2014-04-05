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
 * Map by convention.
 *
 * @author Rafal Chojnacki
 */
public interface MappingConvention {

    /**
     * Indicates that mapping configuration is defined, so convention may
     * populate its internal configuration.
     *
     * @param mapper mapper which will use this convention.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     */
    void build(Mapper mapper, Class sourceClass, Class destinationClass);

    /**
     * Executes mappings.
     * {@link #build(org.beancp.Mapper, java.lang.Class, java.lang.Class)} must
     * be executed before first execution of this method. Implementation must be
     * thread-safe.
     *
     * @param mapper mapper delegating mapping to this convention.
     * @param source source object.
     * @param destination destination object.
     */
    void execute(Mapper mapper, Object source, Object destination);
}
