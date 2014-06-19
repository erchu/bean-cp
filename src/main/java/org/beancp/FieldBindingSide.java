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

import java.lang.reflect.Field;

/**
 * Binding to field.
 */
public final class FieldBindingSide implements BindingSide {

    private final Field _field;

    /**
     * Creates binding to field from field reference.
     * 
     * @param field field reference used to create binding.
     */
    public FieldBindingSide(final Field field) {
        this._field = field;
    }

    @Override
    @SuppressWarnings({"TooBroadCatch", "UseSpecificCatch"})
    public Object getValue(final Object object) {
        try {
            return _field.get(object);
        } catch (Exception ex) {
            throw new MappingException(String.format("Failed to get value from %s", _field), ex);
        }
    }

    @Override
    @SuppressWarnings({"TooBroadCatch", "UseSpecificCatch"})
    public void setValue(final Object object, final Object value) {
        try {
            _field.set(object, value);
        } catch (Exception ex) {
            throw new MappingException(String.format("Failed to get value from %s", _field), ex);
        }
    }

    @Override
    public String toString() {
        return "Field " + _field.toString();
    }

    @Override
    public Class getValueClass() {
        return _field.getType();
    }

    @Override
    public String getName() {
        return _field.getName();
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
