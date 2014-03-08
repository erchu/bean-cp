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

import javassist.util.proxy.ProxyFactory;


/**
 *
 * @author Rafal Chojnacki
 */
class ProxyBuilder {

    public <T> T createProxy(Class<T> proxiedClass) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(proxiedClass);

        Class proxyClass = proxyFactory.createClass();

        try {
            T proxy = (T) proxyClass.newInstance();

            return proxy;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MapConfigurationException(String.format(
                    "Internal mapper error. Failed to create proxy class for %s",
                    proxiedClass.getName()), ex);
        }
    }
}
