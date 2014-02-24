/*
 * ObjectMapper4j
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
package org.objectmapper4j;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import javassist.util.proxy.MethodHandler;


/**
 * @author Rafal Chojnacki
 */
public class TrackExecutedMethodHandler implements MethodHandler {

    private final Collection<Method> extecuted = new LinkedList<>();

    @Override
    public Object invoke(final Object self, final Method thisMethod,
            final Method proceed, final Object[] args) throws Throwable {
        extecuted.add(thisMethod);

        return proceed.invoke(self, args);
    }

    public void reset() {
        extecuted.clear();
    }

    public Iterable<Method> getExecuted() {
        return extecuted;
    }
}
