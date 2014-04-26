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
public interface MappingConvention {

    /**
     * Indicates that mapping configuration is defined, so convention may populate its internal
     * configuration.
     *
     * @param mappingInfo mapper which will use this convention.
     * @param sourceClass source object class.
     * @param destinationClass destination object class.
     *
     * @see org.beancp.Map#useConvention(org.beancp.MappingConvention)
     * @see org.beancp.MapperBuilder#mapAnyByConvention(org.beancp.MappingConvention)
     */
    void build(MappingsInfo mappingInfo, Class sourceClass, Class destinationClass)
            throws MapperConfigurationException;

    /**
     * Executes mappings. Implementation should handle two scenarios:
     *
     * <ol>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingsInfo, java.lang.Class, java.lang.Class)} <b>is not
     * executed</b>
     * before first execution of this method</li>
     * <li>when for particular instance
     * {@link #build(org.beancp.MappingsInfo, java.lang.Class, java.lang.Class) } <b>is executed</b>
     * before first execution of this method (but <b>never</b> concurrently or after first call on
     * this method)</li>
     * </ol>
     *
     * <p>
     * Implementation must be thread-safe in both of those scenarios. Implementation cannot produce
     * state that is shared state between calls, but may use data produced by
     * {@link #build(org.beancp.MappingsInfo, java.lang.Class, java.lang.Class)} method. Acquiring
     * locks is not permitted.
     * </p>
     *
     * @param mapper mapper delegating mapping to this convention.
     * @param source source object.
     * @param destination destination object.
     *
     * @see org.beancp.Map#useConvention(org.beancp.MappingConvention)
     * @see org.beancp.MapperBuilder#mapAnyByConvention(org.beancp.MappingConvention)
     */
    void execute(Mapper mapper, Object source, Object destination) throws MappingException;
}
