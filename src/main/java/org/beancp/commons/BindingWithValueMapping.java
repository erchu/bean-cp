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
import static org.beancp.ConstraintUtils.failIfNull;

class BindingWithValueMapping extends Binding {

    public BindingWithValueMapping(
            final BindingSide[] fromBindingSide, final BindingSide toBindingSide) {
        super(fromBindingSide, toBindingSide);
    }

    public BindingWithValueMapping(
            final BindingSide fromBindingSide, final BindingSide toBindingSide) {
        super(fromBindingSide, toBindingSide);
    }

    @Override
    protected void setValueAtDestination(
            final Mapper mapper, final Object destination, final Object value) {
        failIfNull(mapper, "mapper");
        failIfNull(destination, "destination");

        BindingSide toBindingSide = getToBindingSide();

        if (toBindingSide.isGetterAvailable()) {
            Object currentValue = toBindingSide.getValue(destination);

            if (currentValue != null) {
                mapper.map(value, currentValue);
            } else {
                Object mapResult = mapper.map(value, toBindingSide.getValueClass());
                super.setValueAtDestination(mapper, destination, mapResult);
            }
        } else {
            Object mapResult = mapper.map(value, toBindingSide.getValueClass());
            super.setValueAtDestination(mapper, destination, mapResult);
        }
    }
}
