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

import java.util.List;

/**
 * Mapping convention.
 */
public interface MapConvention {

    /**
     * Returns list o bindings for specified source and destination classes;
     *
     * @param mappingsInfo current mapping information.
     * @param sourceClass source class.
     * @param destinationClass destination class.
     * @return found bindings.
     */
    List<Binding> getBindings(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass);
}
