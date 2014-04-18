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
 * Map by convention configuration.
 */
public class DefaultMapConvention implements MapConvention {

    /**
     * Constructs instance.
     */
    protected DefaultMapConvention() {
    }

    /**
     * Returns default mapping convention.
     *
     * @return default mapping convention.
     */
    public static DefaultMapConvention get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Sets list of destination members which will be ignored (not mapped) by
     * convention.
     *
     * @param members members to ignore
     *
     * @return this (for method chaining)
     */
    public DefaultMapConvention ignoreDestinationMembers(String... members) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Only direct, primitive type (according to isPrimitive() method of Type
     * class)and Strings will be mapped by convention.
     *
     * @return this (for method chaining)
     */
    public DefaultMapConvention forPrimitiveTypeAndStringMembersOnly() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void build(Mapper mapper, Class sourceClass, Class destinationClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute(Mapper mapper, Object source, Object destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
