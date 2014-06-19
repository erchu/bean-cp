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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import static org.apache.commons.lang3.Validate.*;

/**
 * Binding to property.
 */
public class PropertyBindingSide implements BindingSide {

    private final Method _readMethod;

    private final Method _writeMethod;

    private final String _name;

    private final Class _valueClass;

    /**
     *
     * Creates binding to property from property information.
     *
     * @param propertyDescriptor property information used to create binding
     */
    public PropertyBindingSide(final PropertyDescriptor propertyDescriptor) {
        this._valueClass = propertyDescriptor.getPropertyType();
        this._readMethod = propertyDescriptor.getReadMethod();
        this._writeMethod = propertyDescriptor.getWriteMethod();
        this._name = propertyDescriptor.getName();
    }

    @Override
    @SuppressWarnings({ "TooBroadCatch", "UseSpecificCatch" })
    public Object getValue(final Object object) {
        isTrue(_readMethod != null, "Getter is not available.");

        try {
            return _readMethod.invoke(object);
        } catch (Exception ex) {
            throw new MappingException(
                    String.format("Failed to get value from %s", _readMethod), ex);
        }
    }

    @Override
    @SuppressWarnings({ "TooBroadCatch", "UseSpecificCatch" })
    public void setValue(final Object object, final Object value) {
        isTrue(_writeMethod != null, "Setter is not available.");

        try {
            _writeMethod.invoke(object, value);
        } catch (Exception ex) {
            throw new MappingException(
                    String.format("Failed to get value from %s", _readMethod), ex);
        }
    }

    @Override
    public String toString() {
        return String.format("Property - read method: %s, write method: %s",
                _readMethod, _writeMethod);
    }

    @Override
    public Class getValueClass() {
        return _valueClass;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean isGetterAvailable() {
        return (_readMethod != null);
    }

    @Override
    public boolean isSetterAvailable() {
        return (_writeMethod != null);
    }
}
