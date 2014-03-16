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
package org.beancp;

import java.util.function.Supplier;


/**
 * @author Rafal Chojnacki
 */
public final class BindingOption {

    private BindingOption() {
    }

    public static <S> BindingOption mapWhen(final Supplier<Boolean> condition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static <S> BindingOption ignore() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static <T> BindingOption withNullSubstitution(final Supplier<T> nullSubstitution) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
