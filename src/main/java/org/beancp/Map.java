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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Rafal Chojnacki
 */
public interface Map<S, D> {

    /**
     * Adds mapping from <b>single</b> source member to <b>single</b> destination member.
     *
     * @param <T> value data type
     * @param fromMember source class member
     * @param toMember destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindOneToOne(final Supplier<T> fromMember, final Consumer<T> toMember,
            final BindingOption... options);

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
    <T> Map<S, D> bindFunction(final Supplier<T> supplierFunction, final Consumer<T> toMember,
            final BindingOption... options);

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
    <T> Map<S, D> bindConstant(final T constantValue, final Consumer<T> toMember,
            final BindingOption... options);

    Map<S, D> convertUsing(final Action action);

    Map<S, D> useConvention(final MappingConvention mappingConvention);

    Map<S, D> enableAnnotationMapping();

    <T> Map<S, D> setOption(final Consumer<T> destinationMember, final BindingOption... options);

    Map<S, D> afterMap(final Action action);

    <T> Map<S, D> afterMemberMap(final Consumer<T> destinationMember, final Action action);

    Map<S, D> afterAnyMemberMap(final Consumer<String> action);

    Map<S, D> beforeMap(final Action action);

    <T> Map<S, D> beforeMemberMap(final Consumer<T> destinationMember, final Action action);

    Map<S, D> beforeAnyMemberMap(final Consumer<String> action);

    Map<S, D> constructDestinationObjectUsing(final Supplier<D> constructor);

    Map<S, D> constructDestinationObjectUsing(final Function<S, D> constructor);

    <T> Map<S, D> verifyAllDestinationPropertiesConfigured();

    <T> Map<D, S> withReverseMap(final ReverseMapOption reverseMapOption);
}
