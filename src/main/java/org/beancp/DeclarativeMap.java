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
 * Defines mapping between source and destination class. Source and destination classes must have
 * default public or private constructor. Interface methods must be executed in following order:
 *
 * <ol>
 * <li>{@link #constructDestinationObjectUsing(java.util.function.Supplier)} zero or one time</li>
 * <li>{@link #beforeMap(org.beancp.Action)} zero or many times</li>
 * <li>{@link #useConvention(org.beancp.MapConvention) } zero or one time</li>
 * <li>{@link #bind(java.util.function.Supplier, java.util.function.Consumer, org.beancp.BindingOption...)},
 * {@link #bindConstant(java.lang.Object, java.util.function.Consumer, org.beancp.BindingOption...)},
 * {@link #mapInner(java.util.function.Supplier, java.util.function.Consumer, java.lang.Class, org.beancp.BindingOption[])}
 * and
 * {@link #mapInner(java.util.function.Supplier, java.util.function.Consumer, java.util.function.Supplier, java.lang.Class, org.beancp.BindingOption[])}
 * methods zero or many times in any order</li>
 * <li>{@link #afterMap(org.beancp.Action)} zero or many times</li>
 * </ol>
 *
 * @param <S> source class
 * @param <D> destination class
 */
public interface DeclarativeMap<S, D> {

    /**
     * Operation used to build destination object. Must be thread-safe.
     *
     * @param destinationObjectBuilder destination object builder, must be thread-safe.
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> constructDestinationObjectUsing(final Supplier<D> destinationObjectBuilder);

    /**
     * Action to be performed after mappings. Must be thread-safe.
     *
     * @param action action to be executed after mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> beforeMap(final Action action);

    /**
     * Action to be performed after mappings. Must be thread-safe.
     *
     * @param action action to be executed after mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> beforeMap(final Consumer<Mapper> action);

    /**
     * Adds mappings using convention. Convention mappings are performed before other mappings
     * defined by
     * {@link #bind(java.util.function.Supplier, java.util.function.Consumer, org.beancp.BindingOption...)}
     * and
     * {@link #bindConstant(java.lang.Object, java.util.function.Consumer, org.beancp.BindingOption...)}.
     *
     * @param MapConvention convention to use. Must be thread-safe.
     *
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> useConvention(final MapConvention MapConvention);

    /**
     * Adds calculated member binding to destination member or members. Must be thread-safe.
     *
     * @param <T> value data type
     * @param fromFunction calculated member function, must be thread-safe.
     * @param toMember destination class member, must be thread-safe.
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> DeclarativeMap<S, D> bind(
            final Supplier<T> fromFunction,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds constant binding to destination member or members. Must be thread-safe.
     *
     * @param <T> value data type
     * @param constantValue constant value
     * @param toMember destination class member, must be thread-safe.
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> DeclarativeMap<S, D> bindConstant(
            final T constantValue,
            final Consumer<T> toMember,
            final BindingOption<S, D, T>... options);

    /**
     * Adds inner object mapping. Must be thread-safe.
     *
     * @param <SI> source value data type
     * @param <DI> destination value data type
     * @param fromFunction calculated member function, must be thread-safe.
     * @param toMember destination class member setter, must be thread-safe.
     * @param toMemberGetter destination class member get, must be thread-safe.
     * @param toMemberClass destination class member type
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <SI, DI> DeclarativeMap<S, D> mapInner(
            final Supplier<SI> fromFunction,
            final Consumer<DI> toMember,
            final Supplier<DI> toMemberGetter,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options);

    /**
     * Adds inner object mapping. Must be thread-safe.
     *
     * @param <SI> source value data type
     * @param <DI> destination value data type
     * @param fromFunction calculated member function, must be thread-safe.
     * @param toMember destination class member setter, must be thread-safe.
     * @param toMemberClass destination class member type
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <SI, DI> DeclarativeMap<S, D> mapInner(
            final Supplier<SI> fromFunction,
            final Consumer<DI> toMember,
            final Class<DI> toMemberClass,
            final BindingOption<S, D, DI>... options);

    /**
     * Action to be performed before mappings. Must be thread-safe.
     *
     * @param action action to be executed before mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> afterMap(final Action action);

    /**
     * Action to be performed before mappings. Must be thread-safe.
     *
     * @param action action to be executed before mappings, must be thread-safe.
     *
     * @return this (for method chaining)
     */
    DeclarativeMap<S, D> afterMap(final Consumer<Mapper> action);
}
