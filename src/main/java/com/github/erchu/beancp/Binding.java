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
package com.github.erchu.beancp;

import static org.apache.commons.lang3.Validate.*;

/**
 * Binding from source member to destination member (defined using reflection).
 *
 * @see MapConvention#getBindings(com.github.erchu.beancp.MappingInfo, java.lang.Class,
 * java.lang.Class)
 */
public class Binding {

    private final BindingSide[] _sourcePath;

    private final BindingSide _destinationMember;

    /**
     * Creates binding from from source path (series of bindings from source where n+1 binding
     * returns member of object returned by n-th binding) to destination member.
     *
     * @param sourcePath series of bindings from source where n+1 binding returns member of object
     * returned by n-th binding.
     * @param destinationMember destination member.
     */
    public Binding(final BindingSide[] sourcePath, final BindingSide destinationMember) {
        notNull(sourcePath, "sourcePath");
        isTrue(sourcePath.length > 0, "sourcePath is empty");
        notNull(destinationMember, "destinationMember");

        this._sourcePath = sourcePath;
        this._destinationMember = destinationMember;
    }

    /**
     * Creates Binding from source member to destination member.
     *
     * @param sourceMember source member.
     * @param destinationMember destination member.
     */
    public Binding(final BindingSide sourceMember, final BindingSide destinationMember) {
        this(new BindingSide[] { sourceMember }, destinationMember);
    }

    /**
     * Returns binding source path (series of bindings from source where n+1 binding returns member
     * of object returned by n-th binding).
     *
     * @return source path (series of bindings from source where n+1 binding returns member of
     * object returned by n-th binding).
     */
    public BindingSide[] getSourcePath() {
        return _sourcePath;
    }

    /**
     * Returns destination member.
     *
     * @return destination member.
     */
    public BindingSide getDestinationMember() {
        return _destinationMember;
    }

    /**
     * Copies value from source to destination.
     *
     * @param mapper caller.
     * @param source source object
     * @param destination destination object.
     * @throws MappingException when copy action fails.
     */
    public void execute(final Mapper mapper, final Object source, final Object destination)
            throws MappingException {
        Object value = source;

        for (BindingSide i : _sourcePath) {
            if (value == null) {
                setValueAtDestination(mapper, destination, null);
                return;
            }

            value = getValue(i, value);
        }

        setValueAtDestination(mapper, destination, value);
    }

    /**
     * Sets value at destination.
     *
     * @param mapper caller.
     * @param destination destination object.
     * @param value value to set.
     */
    protected void setValueAtDestination(
            final Mapper mapper, final Object destination, final Object value) {
        notNull(destination, "destination");

        _destinationMember.setValue(destination, value);
    }

    private Object getValue(final BindingSide bindingSide, final Object object) {
        notNull(bindingSide, "bindingSide");
        notNull(object, "object");

        if (_destinationMember.isGetterAvailable() == false) {
            throw new MappingException("Getter not available for " + bindingSide);
        }

        Object getValueResult = bindingSide.getValue(object);

        return getValueResult;
    }
}
