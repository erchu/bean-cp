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
package com.github.erchu.beancp;

import java.util.Optional;

/**
 * Object to object mapper. Implementation must be thread-safe.
 */
public interface Mapper extends MappingInfo {

    /**
     * Copies data from source object to destination object.
     *
     * <p>
     * When the are more than one mapper valid for source and destination classes then mapper is
     * chose according to priority (first option has highest priority):
     * </p>
     *
     * <ol>
     * <li>source class equals to mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class equals to mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>convention defined by
     * {@link MapperBuilder#addMapAnyByConvention(com.github.erchu.beancp.MapConvention...)}</li>
     * </ol>
     *
     * <p>
     * When there is more than one mapper of the same priority then is used the first one added to
     * {@link MapperBuilder}. If no mapper available then {@link MappingException} will be thrown.
     * </p>
     *
     * <p>
     * Circular references will result in stack overflow.
     * </p>
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param destination destination object.
     */
    <S, D> void map(S source, D destination) throws MappingException;

    /**
     * Copies data from source object to destination object.
     *
     * <p>
     * When the are more than one mapper valid for source and destination classes then mapper is
     * chose according to priority (first option has highest priority):
     * </p>
     *
     * <ol>
     * <li>source class equals to mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class equals to mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>convention defined by
     * {@link MapperBuilder#addMapAnyByConvention(com.github.erchu.beancp.MapConvention...)}</li>
     * </ol>
     *
     * <p>
     * When there is more than one mapper of the same priority then is used the first one added to
     * {@link MapperBuilder}. If no mapper available then will return false, otherwise
     * return true.
     *
     * <p>
     * Circular references will result in stack overflow.
     * </p>
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param destination destination object.
     * @return false if no mapper available, otherwise true.
     */
    <S, D> boolean mapIfMapperAvailable(S source, D destination) throws MappingException;

    /**
     * Constructs destination object and copies data from source object to newly created destination
     * object. Destination object is created by destination object builder defined by
     * {@link DeclarativeMap#constructDestinationObjectUsing(java.util.function.Supplier)} or if
     * destination object builder is not available by default constructor.
     *
     * <p>
     * When the are more than one mapper valid for source and destination classes then mapper is
     * chose according to priority (first option has highest priority):
     * </p>
     *
     * <ol>
     * <li>source class equals to mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class equals to mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>convention defined by
     * {@link MapperBuilder#addMapAnyByConvention(com.github.erchu.beancp.MapConvention...)}</li>
     * </ol>
     *
     * <p>
     * When there is more than one mapper of the same priority then is used the first one added to
     * {@link MapperBuilder}. If no mapper available then {@link MappingException} will be thrown.
     * </p>
     *
     * <p>
     * Circular references will result in stack overflow.
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param destinationClass destination object class.
     * @return destination object.
     */
    <S, D> D map(S source, Class<D> destinationClass) throws MappingException;

    /**
     * Constructs destination object and copies data from source object to newly created destination
     * object. Destination object is created by destination object builder defined by
     * {@link DeclarativeMap#constructDestinationObjectUsing(java.util.function.Supplier)} or if
     * destination object builder is not available by default constructor.
     *
     * <p>
     * When the are more than one mapper valid for source and destination classes then mapper is
     * chose according to priority (first option has highest priority):
     * </p>
     *
     * <ol>
     * <li>source class equals to mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class equals to mapper
     * destination class</li>
     * <li>source class equals to mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>source class inherits from mapper source class and destination class inherits from mapper
     * destination class</li>
     * <li>convention defined by
     * {@link MapperBuilder#addMapAnyByConvention(com.github.erchu.beancp.MapConvention...)}</li>
     * </ol>
     *
     * <p>
     * When there is more than one mapper of the same priority then is used the first one added to
     * {@link MapperBuilder}.
     * </p>
     *
     * <p>
     * Circular references will result in stack overflow.
     * </p>
     *
     * @param <S> source object class.
     * @param <D> destination object class.
     * @param source source object.
     * @param destinationClass destination object class.
     * @return destination object if no mapper is available, otherwise empty optional object.
     */
    <S, D> Optional<D> mapIfMapperAvailable(S source, Class<D> destinationClass) throws MappingException;
}
