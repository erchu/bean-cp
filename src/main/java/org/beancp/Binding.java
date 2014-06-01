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

import static org.apache.commons.lang3.Validate.*;

/**
 * Binding from source member to destination member defined using reflection.
 *
 * @see MapConvention#getBindings(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)
 */
public class Binding {

    private final BindingSide[] sourcePath;

    private final BindingSide destinationMember;

    /**
     * Creates binding from series of bindings from source (path) to destination member.
     *
     * @param sourcePath series of bindings from source where n+1 binding returns field or property
     * value from object returned by n-th binding.
     * @param destinationMember destination member.
     */
    public Binding(final BindingSide[] sourcePath, final BindingSide destinationMember) {
        notNull(sourcePath, "sourcePath");
        isTrue(sourcePath.length > 0, "sourcePath is empty");
        notNull(destinationMember, "destinationMember");

        this.sourcePath = sourcePath;
        this.destinationMember = destinationMember;
    }

    /**
     * Creates biding from source member to destination member.
     *
     * @param sourceMember source member.
     * @param destinationMember destination member.
     */
    public Binding(final BindingSide sourceMember, final BindingSide destinationMember) {
        this(new BindingSide[] { sourceMember }, destinationMember);
    }

    /**
     * Returns binding source path (series of bindings from source where n+1 binding returns value
     * from object (value) returned by n-th binding).
     *
     * @return source path (series of bindings from source where n+1 binding returns field or
     * property value from object returned by n-th binding).
     */
    public BindingSide[] getSourcePath() {
        return sourcePath;
    }

    /**
     * Returns destination member.
     *
     * @return destination member.
     */
    public BindingSide getDestinationMember() {
        return destinationMember;
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

        for (BindingSide i : sourcePath) {
            if (value == null) {
                setValueAtDestination(mapper, destination, null);
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
        destinationMember.setValue(destination, value);
    }

    private Object getValue(final BindingSide bindingSide, final Object object) {
        if (destinationMember.isGetterAvailable() == false) {
            throw new MappingException("Getter not available for " + bindingSide);
        }

        Object getValueResult = bindingSide.getValue(object);

        return getValueResult;
    }
}
