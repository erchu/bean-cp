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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Rafal Chojnacki
 */
public interface Map<S, D> {

    Map<S, D> afterMap(final BiConsumer<S, D> action);

    <T> Map<S, D> afterMemberMap(final BiConsumer<D, T> member, final BiConsumer<S, D> action);

    Map<S, D> afterMemberMap(final TriConsumer<S, D, String> action);

    Map<S, D> beforeMap(final BiConsumer<S, D> action);

    <T> Map<S, D> beforeMemberMap(final BiConsumer<D, T> member, final BiConsumer<S, D> action);

    Map<S, D> beforeMemberMap(final TriConsumer<S, D, String> action);

    /**
     * Adds calculated member binding to destination member or members.
     *
     * @param <T> value data type
     * @param from calculated member function
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindFunction(final Supplier<T> from, final Consumer<T> to,
            final BindingOption... options);

    /**
     * Adds mapping from <b>single</b> source member to <b>single</b> destination member.
     *
     * @param <T> value data type
     * @param from source class member
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindOneToOne(final Supplier<T> from, final Consumer<T> to,
            final BindingOption... options);

    /**
     * Adds constant binding to destination member or members.
     *
     * @param <T> value data type
     * @param constantValue constant value
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    <T> Map<S, D> bindConstant(final T constantValue, final Consumer<T> to,
            final BindingOption... options);

    <T> Map<S, D> bindByConvention(final BiConsumer<D, T> member,
            final MappingConvention convention, final BindingOption... options);

    Map<S, D> constructDestinationObjectUsing(final Supplier<D> action);

    Map<S, D> constructDestinationObjectUsing(final Function<S, D> action);

    Map<S, D> convertUsing(final BiConsumer<S, D> action);

    <T> Map<S, D> setOption(final BiConsumer<D, T> member, final BindingOption... options);

    Map<S, D> useConvention(final MappingConvention mappingConvention);

    <T> Map<S, D> verifyAllDestinationPropertiesConfigured();

    <T> Map<D, S> withReverseMap(final ReverseMapOption reverseMapOption);
}
