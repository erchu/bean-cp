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
 * Indicates error in mappings configuration.
 */
public class MapperConfigurationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>MapDefinitionException</code> without detail message.
     */
    public MapperConfigurationException() {
    }

    /**
     * Constructs an instance of <code>MapDefinitionException</code> with the specified detail
     * message.
     *
     * @param message The detail message.
     */
    public MapperConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs an instance of <code>MapDefinitionException</code> with the specified inner
     * exception.
     *
     * @param throwable The inner exception.
     */
    public MapperConfigurationException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructs an instance of <code>MapDefinitionException</code> with the specified detail
     * message and inner exception.
     *
     * @param message The detail message.
     * @param throwable The inner exception.
     */
    public MapperConfigurationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
