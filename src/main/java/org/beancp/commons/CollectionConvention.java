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

import java.util.List;
import org.beancp.Binding;
import org.beancp.MapConvention;
import org.beancp.MappingInfo;

public class CollectionConvention implements MapConvention {
    
    private CollectionConvention() {}
    
    public static CollectionConvention get() {
        return new CollectionConvention();
    }

    @Override
    public List<Binding> getBindings(
            final MappingInfo mappingsInfo, final Class sourceClass, final Class destinationClass) {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
