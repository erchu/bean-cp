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

package org.beancp.commons;

import org.beancp.Mapper;
import org.beancp.MapperConfigurationException;
import org.beancp.MapConvention;
import org.beancp.MappingException;
import org.beancp.MappingInfo;

public class StringParseConvention implements MapConvention {
    
    private StringParseConvention() {}
    
    public static StringParseConvention get() {
        return new StringParseConvention();
    }

    @Override
    public void build(
            final MappingInfo mappingInfo, final Class sourceClass, final Class destinationClass)
            throws MapperConfigurationException {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void map(
            final Mapper mapper, final Object source, final Object destination)
            throws MappingException {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean tryMap(final Mapper mapper, final Object source, final Object destination)
            throws MappingException {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canMap(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass) {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
