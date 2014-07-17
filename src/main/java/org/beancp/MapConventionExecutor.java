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

import java.util.List;
import static org.apache.commons.lang3.Validate.notNull;

class MapConventionExecutor {

    private final MapConvention _convention;

    private List<Binding> _bindings = null;

    public MapConventionExecutor(final MapConvention convention) {
        _convention = convention;
    }

    /**
     * Indicates that mapping configuration is defined, so convention may populate its internal
     * configuration.
     *
     * @param mappingInfo mapper which will use this convention.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     *
     * @see org.beancp.DeclarativeMap#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    void build(MappingInfo mappingInfo, Class sourceClass, Class destinationClass)
            throws MapperConfigurationException {
        _bindings = _convention.getBindings(mappingInfo, sourceClass, destinationClass);
    }

    /**
     * Executes mappings. Implementation should handle two scenarios:
     *
     * <ol>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} <b>is not
     * executed</b> before first execution of this method</li>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} <b>is executed</b>
     * before first execution of this method (but <b>never</b> concurrently or after first call on
     * this method)</li>
     * </ol>
     *
     * <p>
     * Implementation must be thread-safe in both of those scenarios. Implementation cannot produce
     * state that is shared state between calls, but may use data produced by
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} method. Acquiring
     * locks is not permitted.
     * </p>
     *
     * <p>
     * If mapping is not supported {@link MappingException} will be thrown.
     * </p>
     *
     * @param mapper mapper delegating mapping to this convention.
     * @param source source object.
     * @param destination destination object.
     *
     * @see org.beancp.DeclarativeMap#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    void map(Mapper mapper, Object source, Object destination) throws MappingException {
        if (tryMap(mapper, source, destination) == false) {
            throw new MappingException(String.format("I don't know how to map %s to %s",
                    source.getClass(), destination.getClass()));
        }
    }

    /**
     * Executes mappings. Implementation should handle two scenarios:
     *
     * <ol>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)}
     * <b>is not executed</b>
     * before first execution of this method</li>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} <b>is executed</b>
     * before first execution of this method (but <b>never</b> concurrently or after first call on
     * this method)</li>
     * </ol>
     *
     * <p>
     * Implementation must be thread-safe in both of those scenarios. Implementation cannot produce
     * state that is shared state between calls, but may use data produced by
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} method. Acquiring
     * locks is not permitted.
     * </p>
     *
     * <p>
     * If mapping is supported for passed object types will return {@code}false{code}, otherwise
     * return {@code}true{code}.
     * </p>
     *
     * @param mapper mapper delegating mapping to this convention.
     * @param source source object.
     * @param destination destination object.
     * @return {@code}true{code} if mapping is supported for passed object types, otherwise
     * {@code}true{code}.
     *
     * @see org.beancp.DeclarativeMap#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    boolean tryMap(Mapper mapper, Object source, Object destination)
            throws MapperConfigurationException {
        notNull(mapper, "mapper");
        notNull(source, "source");
        notNull(destination, "destination");

        List<Binding> bindingsToExecute = getBindingsToExecute(
                mapper, source.getClass(), destination.getClass());

        if (bindingsToExecute.isEmpty()) {
            return false;
        } else {
            executeBindings(bindingsToExecute, mapper, source, destination);

            return true;
        }
    }

    /**
     * Returns {@code}true{code} mapping is supported for passed object types will return
     * {@code}false{code}, otherwise return {@code}true{code}.
     *
     * @param mappingsInfo available mappings information.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @return {@code}true{code} if mapping is supported for passed object types, otherwise
     * {@code}true{code}.
     */
    public boolean canMap(MappingInfo mappingsInfo, Class sourceClass, Class destinationClass) {
        notNull(mappingsInfo, "mappingsInfo");
        notNull(sourceClass, "source");
        notNull(destinationClass, "destination");

        List<Binding> bindingsToExecute = getBindingsToExecute(
                mappingsInfo, sourceClass, destinationClass);

        return (bindingsToExecute.isEmpty() == false);
    }

    private List<Binding> getBindingsToExecute(
            final MappingInfo mappingsInfo, final Class sourceClass, final Class destinationClass) {
        // According to API specification build() method but never concurrently or after first of
        // this method, so we can safely get bindings field value without acquiring any locks or
        // defining fields as volatile.
        List<Binding> bindingsToExecute = (_bindings != null)
                ? _bindings
                // According to API specification it is build() method may be not executed before
                // this method call. In this situation we generate bindings on the fly. Moreover API
                // prohibits produce state that is shared state between calls, so next call will
                // generate bindings once again.
                : getBindings(mappingsInfo, sourceClass, destinationClass);

        return bindingsToExecute;
    }

    private void executeBindings(final List<Binding> bindingsToExecute, final Mapper mapper,
            final Object source, final Object destination) {
        bindingsToExecute.stream().forEach(i -> {
            i.execute(mapper, source, destination);
        });
    }

    private List<Binding> getBindings(
            final MappingInfo mappingsInfo, final Class sourceClass, final Class destinationClass) {
        return _convention.getBindings(mappingsInfo, sourceClass, destinationClass);
    }
}
