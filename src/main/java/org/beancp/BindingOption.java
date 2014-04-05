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

import java.util.function.Supplier;

/**
 * Binding options used in
 * {@link Map#bind(java.util.function.Supplier, java.util.function.Consumer, org.beancp.BindingOption...)}
 * and
 * {@link Map#bindConstant(java.lang.Object, java.util.function.Consumer, org.beancp.BindingOption...)}.
 *
 * @author Rafal Chojnacki
 */
public final class BindingOption {

    private BindingOption() {
    }

    /**
     * Property mapping will be performed when condition lambda will return
     * true.
     *
     * @param condition mapping condition lambda.
     * @return this (for method chaining)
     */
    public static BindingOption mapWhen(final Supplier<Boolean> condition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     *
     * @param <S> source object type.
     * @param nullSubstitution null substitution lambda.
     * @return this (for method chaining)
     */
    public static <S> BindingOption withNullSubstitution(final Supplier<S> nullSubstitution) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
