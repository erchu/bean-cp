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
import java.util.function.Function;
import java.util.function.Supplier;


/**
 *
 * @param <S> Source class
 * @param <D> Destination Class
 * @author Rafal Chojnacki
 */
public abstract class MapDefinition<S, D> {

    protected MapDefinition<S, D> bindUsingConvention(final MappingConvention mappingConvention) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> bindFromMember(
            final Function<S, T> source,
            final BiConsumer<D, T> destination,
            BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> bindFromFunction(
            final Function<S, T> source,
            final BiConsumer<D, T> destination,
            BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> bindFromConstant(
            final T constantValue,
            final BiConsumer<D, T> destination,
            BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> ignore(
            final BiConsumer<D, T> destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> verifyAllDestinationPropertiesMapped(
            final BiConsumer<D, T> destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> beforeMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> afterMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> beforeMapMember(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> afterMapMember(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> convertUsing(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> constructUsing(final Supplier<D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected MapDefinition<S, D> includingSourceSupperClasses(Class... clazz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected MapDefinition<S, D> includingAllSourceSupperClasses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected MapDefinition<S, D> includingDestinationSupperClasses(Class... clazz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected MapDefinition<S, D> includingAllDestinationSupperClasses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<D, S> reverseMap(final ReverseMapOption reverseMapOption) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
