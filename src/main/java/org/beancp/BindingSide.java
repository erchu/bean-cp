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
 * Source or destination part (for example property) referenced by binding.
 */
public interface BindingSide {

    /**
     * Returns name (ex. property name).
     *
     * @return name (ex. property name).
     */
    String getName();

    /**
     * Returns value class.
     *
     * @return value class.
     */
    Class getValueClass();

    /**
     * Extracts value from passed object and returns it. If there is no getter available then
     * {@link MappingException) will be trown.
     *
     * @param object value source.
     * @return value for passed object.
     */
    Object getValue(final Object object) throws MappingException;

    /**
     * Sets value for passed object. If there is no setter available then
     * {@link MappingException) will be trown.
     *
     * @param object
     * @param value
     */
    void setValue(final Object object, final Object value) throws MappingException;

    /**
     * Returns {@code true} if getter is available, otherwise {@code false}.
     * 
     * @return {@code true} if getter is available, otherwise {@code false}.
     */
    boolean isGetterAvailable();

    /**
     * Returns {@code true} if setter is available, otherwise {@code false}.
     * 
     * @return {@code true} if setter is available, otherwise {@code false}.
     */
    boolean isSetterAvailable();
}
