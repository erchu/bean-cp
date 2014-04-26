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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Optional;
import org.beancp.MappingException;
import static org.beancp.Util.failIfTrue;

class PropertyBindingSide implements BindingSide {

    private final Method readMethod;

    private final Method writeMethod;

    private final String name;

    private final Class valueClass;

    public PropertyBindingSide(final PropertyDescriptor propertyDescriptor) {
        this.valueClass = propertyDescriptor.getPropertyType();
        this.readMethod = propertyDescriptor.getReadMethod();
        this.writeMethod = propertyDescriptor.getWriteMethod();
        this.name = propertyDescriptor.getName();
    }

    @Override
    public Object getValue(final Object object) {
        failIfTrue(readMethod == null, "Getter is not available.");

        try {
            return readMethod.invoke(object);
        } catch (Exception ex) {
            throw new MappingException(
                    String.format("Failed to get value from %s", readMethod), ex);
        }
    }

    @Override
    public void setValue(final Object object, final Object value) {
        failIfTrue(writeMethod == null, "Setter is not available.");

        try {
            writeMethod.invoke(object, value);
        } catch (Exception ex) {
            throw new MappingException(
                    String.format("Failed to get value from %s", readMethod), ex);
        }
    }

    @Override
    public String toString() {
        return String.format("Property - read method: %s, write method: %s",
                readMethod, writeMethod);
    }

    @Override
    public Class getValueClass() {
        return valueClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isGetterAvailable() {
        return (readMethod != null);
    }

    @Override
    public boolean isSetterAvailable() {
        return (writeMethod != null);
    }
}
