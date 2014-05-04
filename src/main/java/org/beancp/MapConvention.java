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

/**
 * Mapping convention.
 */
public interface MapConvention {

    /**
     * Indicates that mapping configuration is defined, so convention may populate its internal
     * configuration.
     *
     * @param mappingInfo mapper which will use this convention.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     *
     * @see org.beancp.Map#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    void build(MappingInfo mappingInfo, Class sourceClass, Class destinationClass)
            throws MapperConfigurationException;

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
     * @see org.beancp.Map#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    void map(Mapper mapper, Object source, Object destination) throws MappingException;

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
     * @see org.beancp.Map#useConvention(org.beancp.MapConvention)
     * @see org.beancp.MapperBuilder#addMapAnyByConvention(org.beancp.MapConvention...)
     */
    boolean tryMap(Mapper mapper, Object source, Object destination)
            throws MapperConfigurationException;

    /**
     * Returns {@code}true{code} mapping is supported for passed object types will return
     * {@code}false{code}, otherwise return {@code}true{code}..
     *
     * @param mappingsInfo available mappings information.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     * @return {@code}true{code} if mapping is supported for passed object types, otherwise
     * {@code}true{code}.
     */
    boolean canMap(MappingInfo mappingsInfo, Class sourceClass, Class destinationClass);
}
