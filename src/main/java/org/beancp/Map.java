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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Map configuration.
 *
 * @param <S> source class
 * @param <D> destination class
 * @author Rafal Chojnacki
 */
public interface Map<S, D> {

    /**
     * Adds calculated member binding to destination member or members.
     *
     * @param <T> value data type
     * @param supplierFunction calculated member function
     * @param toMember destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bind(final Supplier<T> supplierFunction,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds constant binding to destination member or members.
     *
     * @param <T> value data type
     * @param constantValue constant value
     * @param toMember destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindConstant(final T constantValue,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds mappings using convention. Convention mappings are performed before
     * other mappings defined by
     * {@link #bind(java.util.function.Supplier, java.util.function.Consumer, org.beancp.BindingOption...)}
     * and
     * {@link #bindConstant(java.lang.Object, java.util.function.Consumer, org.beancp.BindingOption...)}.
     *
     * @param mappingConvention convention to use.
     * @return this (for method chaining)
     */
    Map<S, D> useConvention(final MappingConvention mappingConvention);

    /**
     * Action to be performed after mappings.
     *
     * @param action action to be executed after mappings.
     * @return this (for method chaining)
     */
    Map<S, D> beforeMap(final Action action);

    /**
     * Action to be performed before mappings.
     *
     * @param action action to be executed before mappings.
     * @return this (for method chaining)
     */
    Map<S, D> afterMap(final Action action);

    /**
     * Operation used to build destination object.
     *
     * @param constructor destination object builder.
     * @return this (for method chaining)
     */
    Map<S, D> constructDestinationObjectUsing(final Supplier<D> constructor);
}
