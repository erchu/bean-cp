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
import org.beancp.MappingException;
import static org.apache.commons.lang3.Validate.*;

class Binding {

    private final BindingSide[] fromBindingSide;

    private final BindingSide toBindingSide;

    public Binding(final BindingSide[] fromBindingSide, final BindingSide toBindingSide) {
        notNull(fromBindingSide, "fromBindingSide");
        notNull(toBindingSide, "toBindingSide");

        this.fromBindingSide = fromBindingSide;
        this.toBindingSide = toBindingSide;
    }

    public Binding(final BindingSide fromBindingSide, final BindingSide toBindingSide) {
        this(new BindingSide[] { fromBindingSide }, toBindingSide);
    }

    public BindingSide[] getFromBindingSide() {
        return fromBindingSide;
    }

    public BindingSide getToBindingSide() {
        return toBindingSide;
    }

    public void execute(final Mapper mapper, final Object source, final Object destination)
            throws MappingException {
        Object value = source;

        for (BindingSide i : fromBindingSide) {
            if (value == null) {
                setValueAtDestination(mapper, destination, null);
            }

            value = getValue(i, value);
        }

        setValueAtDestination(mapper, destination, value);
    }

    protected void setValueAtDestination(
            final Mapper mapper, final Object destination, final Object value) {
        toBindingSide.setValue(destination, value);
    }

    private Object getValue(final BindingSide bindingSide, final Object object) {
        if (toBindingSide.isGetterAvailable() == false) {
            throw new MappingException("Getter not available for " + bindingSide);
        }
        
        Object getValueResult = bindingSide.getValue(object);

        return getValueResult;
    }
}
