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

import org.beancp.convention.MappingConvention;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Map configuration.
 *
 * @param <S> source class
 * @param <D> destination class
 */
public interface Map<S, D> {

    /**
     * Adds calculated member binding to destination member or members.
     *
     * @param <T> value data type
     * @param fromFunction calculated member function, must be thread-safe.
     * @param toMember destination class member, must be thread-safe.
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bind(final Supplier<T> fromFunction,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds constant binding to destination member or members.
     *
     * @param <T> value data type
     * @param constantValue constant value
     * @param toMember destination class member, must be thread-safe.
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindConstant(final T constantValue,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds inner object mapping.
     *
     * @param <SI> source value data type
     * @param <DI> destination value data type
     * @param supplierFunction calculated member function, must be thread-safe.
     * @param toMember destination class member setter, must be thread-safe.
     * @param toMemberGetter destination class member get, must be thread-safe.ter
     * @param toMemberClass destination class member type
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <SI, DI> Map<S, D> map(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Supplier<DI> toMemberGetter,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options);

    /**
     * Adds inner object mapping.
     *
     * @param <SI> source value data type
     * @param <DI> destination value data type
     * @param supplierFunction calculated member function, must be thread-safe.
     * @param toMember destination class member setter, must be thread-safe.
     * @param toMemberClass destination class member type
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <SI, DI> Map<S, D> map(final Supplier<SI> supplierFunction,
            final Consumer<DI> toMember,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options);

    /**
     * Adds mappings using convention. Convention mappings are performed before other mappings
     * defined by
     * {@link #bind(java.util.function.Supplier, java.util.function.Consumer, org.beancp.BindingOption...)}
     * and
     * {@link #bindConstant(java.lang.Object, java.util.function.Consumer, org.beancp.BindingOption...)}.
     *
     * @param source source object.
     * @param destination destination object.
     * @param mappingConvention convention to use.
     *
     * @return this (for method chaining)
     */
    Map<S, D> map(final S source, final D destination, final MappingConvention mappingConvention);

    /**
     * Action to be performed after mappings.
     *
     * @param action action to be executed after mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    Map<S, D> beforeMap(final Action action);

    /**
     * Action to be performed before mappings.
     *
     * @param action action to be executed before mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    Map<S, D> afterMap(final Action action);

    /**
     * Operation used to build destination object.
     *
     * @param destinationObjectBuilder destination object builder, must be thread-safe.
     * @return this (for method chaining)
     */
    Map<S, D> constructDestinationObjectUsing(final Supplier<D> destinationObjectBuilder);
}
