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

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;

class FakeObjectBuilder {

    private final ClassPool classPool = ClassPool.getDefault();

    public <T> T createFakeObject(final Class ofClass) {
        Constructor defaultConstructor = getDefaultConstructor(ofClass);

        if (defaultConstructor == null) {
            throw new MapConfigurationException(String.format(
                    "Class %s has no default public or protected constructor "
                    + "or is inner non-static class.",
                    ofClass.toString()));
        }

        if (Modifier.isPublic(defaultConstructor.getModifiers())) {
            try {
                return (T) ofClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MapConfigurationException(String.format(
                        "Failed to instantiate %s class.",
                        ofClass.getName()), ex);
            }
        }

        if (Modifier.isProtected(defaultConstructor.getModifiers())) {
            if (Modifier.isFinal(ofClass.getModifiers())) {
                throw new MapConfigurationException(String.format("Class %s is "
                        + "final and has no public default constructor.",
                        ofClass.getName()));
            }

            Class proxyClass = createProxyClass(ofClass);

            try {
                return (T) proxyClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new MapConfigurationException(String.format(
                        "Failed to instantiate proxy object for %s class.",
                        ofClass.getName()), ex);
            }
        }

        // default constructor is private
        throw new MapConfigurationException(String.format(
                "Class %s has no default public or protected constructor or is "
                + "private class.", ofClass.toString()));
    }

    private Constructor getDefaultConstructor(final Class ofClass) {
        Optional<Constructor> defaultConstructor
                = Arrays.stream(ofClass.getDeclaredConstructors())
                .filter(n -> n.getParameterTypes().length == 0)
                .findAny();

        return (defaultConstructor.isPresent()
                ? defaultConstructor.get() : null);
    }

    private <T> Class createProxyClass(final Class<T> superClass) {
        try {
            classPool.insertClassPath(new ClassClassPath(superClass));

            String proxyClassName = superClass.getName() + "_MapperProxy";
            Class proxyClass;

            try {
                proxyClass = Class.forName(proxyClassName);
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                CtClass superCtClass = classPool.get(superClass.getName());
                CtClass proxyCtClass = classPool.makeClass(proxyClassName);
                proxyCtClass.setSuperclass(superCtClass);

                proxyClass = proxyCtClass.toClass(
                        superClass.getClassLoader(),
                        superClass.getProtectionDomain());

                superCtClass.detach();
                proxyCtClass.detach();
            }

            return proxyClass;
        } catch (NotFoundException | CannotCompileException ex) {
            throw new MapConfigurationException(String.format(
                    "Failed to create proxy class for %s",
                    superClass.getName()), ex);
        }
    }
}
