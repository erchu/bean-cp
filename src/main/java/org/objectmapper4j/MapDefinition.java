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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

/**
 * Defines mapping between source and destination class. Class is not thread safe.
 *
 * @param <S> source class
 * @param <D> destination class
 *
 * @author Rafal Chojnacki
 */
public final class MapDefinition<S, D> {

    private final List<Binding> bindings = new LinkedList<>();

    private Class<S> sourceClass;

    private Class<D> destinationClass;

    private S sourceProxy;

    private D destinationProxy;

    private TrackExecutedMethodHandler sourceProxyMethodHandler;

    private TrackExecutedMethodHandler destinationProxyMethodHandler;

    public MapDefinition(final Class<S> sourceClass, final Class<D> destinationClass) {
        if (sourceClass == null) {
            throw new NullPointerException("Null not allowed for 'source' parameter.");
        }

        if (destinationClass == null) {
            throw new NullPointerException("Null not allowed for 'destination' parameter.");
        }

        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;

        //TODO: Meta-model mapping
        /*
        this.sourceProxyMethodHandler = new TrackExecutedMethodHandler();
        this.sourceProxy = (S) buildProxy(sourceClass, this.sourceProxyMethodHandler);

        this.destinationProxyMethodHandler = new TrackExecutedMethodHandler();
        this.destinationProxy = (D) buildProxy(
                destinationClass, this.destinationProxyMethodHandler);
         */
    }

    private <T> T buildProxy(final Class<T> proxiedClass, final MethodHandler methodHandler) {
        if ((proxiedClass.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
            throw new MapDefinitionException(String.format(
                    "Final classes mapping not supported, but %s is final class.", proxiedClass));
        }

        try {
            Constructor<T> defaultConstructor = proxiedClass.getDeclaredConstructor();

            if ((defaultConstructor.getModifiers() & Modifier.PRIVATE) == Modifier.PRIVATE) {
                throw new MapDefinitionException(String.format(
                        "Classes with no default public or protected constructor are not supported, "
                        + "but %s's default constructor is private.", proxiedClass));
            }
        } catch (NoSuchMethodException ex) {
            throw new MapDefinitionException(String.format(
                    "Classes with no default public or protected constructor are not supported, "
                    + "but %s has no such constructor.", proxiedClass));
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(proxiedClass);

        try {
            Class proxyClass = proxyFactory.createClass();

            T proxy = (T) proxyClass.newInstance();
            ((ProxyObject) proxy).setHandler(methodHandler);

            return proxy;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MapDefinitionException(String.format(
                    "Internal mapper error. Failed to create proxy class for %s",
                    proxiedClass.getName()), ex);
        }
    }

    List<Binding> getBindings() {
        return bindings;
    }

    Class<S> getSourceClass() {
        return sourceClass;
    }

    Class<D> getDestinationClass() {
        return destinationClass;
    }

    protected MapDefinition<S, D> useConvention(final MappingConvention mappingConvention) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Adds mapping between getter method or field of source and setter method or field of
     * destination class.
     *
     * @param <T> value data type
     * @param from source class member
     * @param to destination class member
     * @param options additional mapping options
     *
     * @return this (for method chaining)
     */
    protected <T> MapDefinition<S, D> bindFromMember(
            final Function<S, T> from,
            final BiConsumer<D, T> to,
            final BindingOption... options) {
        //TODO: What with final classes and classes with no public no argument constructor?
        //TODO: Input parameter error checking

        BiConsumer<S, D> bindingExecutor =
                (S source, D destination) -> to.accept(destination, from.apply(source));
        bindings.add(new Binding(bindingExecutor));

        //TODO: Applys only to biding with meta data
        //TODO: - Fields processing (right now only bean getter are supported)
        //TODO: - Check if getter is really getter method
        //TODO: - Check if setter is really setter method
        //TODO: - Check if only one method was called on source/destination proxy
        //TODO: - Check if method is static?
        //TODO: - What if method is final?
        //TODO: - Check if proxies aren't modified during execution
        //TODO: - Proxy reset
        //TODO: Options parameter processing
        return this;
    }

    protected <T> MapDefinition<S, D> bindFromFunction(
            final Function<S, T> from,
            final BiConsumer<D, T> to,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> bindFromConstant(
            final T constantValue,
            final BiConsumer<D, T> to,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> bindByConvention(
            final BiConsumer<D, T> member,
            final MappingConvention convention,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> setOption(
            final BiConsumer<D, T> member,
            final BindingOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> verifyAllDestinationPropertiesConfigured() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> beforeMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> afterMap(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> beforeMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<S, D> afterMemberMap(
            final BiConsumer<D, T> member,
            final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> beforeMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> afterMemberMap(
            final TriConsumer<S, D, String> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> convertUsing(final BiConsumer<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> constructDestinationObjectUsing(final Supplier<D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected MapDefinition<S, D> constructDestinationObjectUsing(final Function<S, D> action) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected <T> MapDefinition<D, S> withReverseMap(final ReverseMapOption reverseMapOption) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
