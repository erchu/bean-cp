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
 * @param <S> source object type.
 * @param <D> destination object type.
 * @param <T> destination member type.
 */
public final class BindingOption<S, D, T> {

    private Supplier<Boolean> _mapWhenCondition = null;

    private T _nullSubstitution = null;

    private BindingOption() {
    }

    Supplier<Boolean> getMapWhenCondition() {
        return _mapWhenCondition;
    }

    void setMapWhenCondition(Supplier<Boolean> mapWhenCondition) {
        this._mapWhenCondition = mapWhenCondition;
    }

    T getNullSubstitution() {
        return _nullSubstitution;
    }

    void setNullSubstitution(T nullSubstitution) {
        this._nullSubstitution = nullSubstitution;
    }

    /**
     * Property mapping will be performed when condition lambda will return true. This option is
     * evaluated first and when evaluates to false no other options are evaluated.
     *
     * @param <S> source object type.
     * @param <D> destination object type.
     * @param <T> destination member type.
     * @param condition mapping condition lambda.
     * @return this (for method chaining)
     */
    public static <S, D, T> BindingOption<S, D, T> mapWhen(final Supplier<Boolean> condition) {
        BindingOption<S, D, T> result = new BindingOption<>();
        result._mapWhenCondition = condition;

        return result;
    }

    /**
     * If from source value getter will return null then value will be substituted with result from
 _nullSubstitution lambda.
     *
     * @param <S> source object type.
     * @param <D> destination object type.
     * @param <T> destination member type.
     * @param nullSubstitution null substitution value.
     * @return this (for method chaining)
     */
    public static <S, D, T> BindingOption<S, D, T> withNullSubstitution(final T nullSubstitution) {
        BindingOption<S, D, T> result = new BindingOption<>();
        result._nullSubstitution = nullSubstitution;

        return result;
    }
}
