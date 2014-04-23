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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.beancp.Mapper;
import org.beancp.MappingException;
import static org.beancp.NullParameterException.failIfNull;

class Binding {

    private final Member[] fromMember;

    private final Member toMember;

    private final Method toMemberMethod;

    private final Field toMemberField;

    public Binding(final Member[] fromMember, final Member toMember) {
        failIfNull(fromMember, "fromMember");
        failIfNull(toMember, "toMember");

        this.fromMember = fromMember;
        this.toMember = toMember;

        this.toMemberMethod = (toMember instanceof Method) ? (Method) toMember : null;
        this.toMemberField = (toMember instanceof Field) ? (Field) toMember : null;

        if (this.toMemberMethod == null && this.toMemberField == null) {
            throw new MappingException(
                    String.format("Not supported member type: %s", toMember.getClass()));
        }
    }

    public Binding(final Member fromMember, final Member toMember) {
        this(new Member[]{fromMember}, toMember);
    }

    public Member[] getFromMember() {
        return (Member[]) fromMember.clone();
    }

    public Member getToMember() {
        return toMember;
    }

    public void execute(final Mapper mapper, final Object source, final Object destination) {
        Object value = source;

        for (Member i : fromMember) {
            if (i instanceof Field) {
                Field iField = (Field) i;

                try {
                    value = iField.get(value);
                } catch (IllegalAccessException ex) {
                    throw new MappingException(
                            String.format("Failed to get value from %s.%s when binding it to %s.%s",
                                    i.getDeclaringClass(), i.getName(),
                                    toMember.getDeclaringClass().getName(), toMember.getName()),
                            ex);
                }

                if (value == null) {
                    setDestinationValue(destination, value);
                }
            } else if (i instanceof Method) {
                Method iMethod = (Method) i;

                try {
                    value = iMethod.invoke(value);

                    if (value == null) {
                        setDestinationValue(destination, value);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new MappingException(
                            String.format("Failed to get value from %s.%s when binding it to %s.%s",
                                    i.getDeclaringClass(), i.getName(),
                                    toMember.getDeclaringClass().getName(), toMember.getName()),
                            ex);
                }
            } else {
                throw new MappingException(
                        String.format("Not supported member type: %s", i.getClass()));
            }
        }

        setDestinationValue(destination, value);
    }

    private void setDestinationValue(final Object destination, final Object value) {
        try {
            if (toMemberField != null) {
                toMemberField.set(destination, value);
            } else if (toMemberMethod != null) {
                toMemberMethod.invoke(destination, value);
            } else {
                // This shouldn't happend is constructor of this class is written correctly
                throw new IllegalArgumentException("Why did we get here?");
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            throw new MappingException(
                    String.format("Failed to set value to %s.%s",
                            toMember.getDeclaringClass().getName(), toMember.getName()),
                    ex);
        }
    }
}
