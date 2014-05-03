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

import java.lang.reflect.Field;
import org.beancp.MappingException;

final class FieldBindingSide implements BindingSide {

    private final Field field;

    public FieldBindingSide(final Field field) {
        this.field = field;
    }

    @Override
    @SuppressWarnings({"TooBroadCatch", "UseSpecificCatch"})
    public Object getValue(final Object object) {
        try {
            return field.get(object);
        } catch (Exception ex) {
            throw new MappingException(String.format("Failed to get value from %s", field), ex);
        }
    }

    @Override
    @SuppressWarnings({"TooBroadCatch", "UseSpecificCatch"})
    public void setValue(final Object object, final Object value) {
        try {
            field.set(object, value);
        } catch (Exception ex) {
            throw new MappingException(String.format("Failed to get value from %s", field), ex);
        }
    }

    @Override
    public String toString() {
        return "Field " + field.toString();
    }

    @Override
    public Class getValueClass() {
        return field.getType();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isGetterAvailable() {
        return true;
    }

    @Override
    public boolean isSetterAvailable() {
        return true;
    }
}
