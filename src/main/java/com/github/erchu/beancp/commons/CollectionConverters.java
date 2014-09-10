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
package com.github.erchu.beancp.commons;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import com.github.erchu.beancp.Converter;
import com.github.erchu.beancp.MappingException;

/**
 * Converters from arrays to collections and from collections to arrays and from collections to
 * collections. Supported destination collection classes:
 *
 * <ul>
 * <li>List</li>
 * <li>NavigableSet</li>
 * <li>Set</li>
 * <li>SortedSet</li>
 * <li>AbstractCollection</li>
 * <li>AbstractList</li>
 * <li>AbstractSequentialList</li>
 * <li>AbstractSet</li>
 * <li>ArrayList</li>
 * <li>Collection</li>
 * <li>ConcurrentSkipListSet</li>
 * <li>CopyOnWriteArrayList</li>
 * <li>CopyOnWriteArraySet</li>
 * <li>HashSet</li>
 * <li>LinkedHashSet</li>
 * <li>LinkedList</li>
 * <li>Stack</li>
 * <li>TreeSet</li>
 * <li>Vector</li>
 * </ul>
 */
public class CollectionConverters {

    private final static Map<Class, Class<? extends Collection>> _defaultCollectionImplementations;

    private final static Converter[] _collectionToCollectionConverters;

    private final static Collection<Class<? extends Collection>> _collectionTypes;

    static {
        _defaultCollectionImplementations = getDefaultCollectionImplementations();
        _collectionTypes = getCollectionTypes();
        _collectionToCollectionConverters = buildCollectionToCollectionConverters(_collectionTypes);
    }

    private CollectionConverters() {
    }

    /**
     * Returns collection to collection converters.
     *
     * @return collection to collection converters.
     */
    public static Converter[] get() {
        return _collectionToCollectionConverters;
    }

    /**
     * Returns collection to array converters.
     *
     * @param <T> element type.
     * @param collectionElementClass element type.
     * @return collection to array converters.
     */
    public static <T> Converter getCollectionToArray(
            final Class<T> collectionElementClass) {
        return new Converter(
                Collection.class,
                getArrayClass(collectionElementClass),
                (Object source) -> {
                    Collection<T> sourceCollection = (Collection<T>) source;

                    Object destination = Array.newInstance(
                            collectionElementClass, sourceCollection.size());

                    int i = 0;

                    for (T item : sourceCollection) {
                        Array.set(destination, i++, item);
                    }

                    return destination;
                }
        );
    }

    /**
     * Returns array to collection converters.
     *
     * @param <T> element type.
     * @param collectionElementClass element type.
     * @return array to collection converters.
     */
    public static <T> Converter[] getArrayToCollection(
            final Class<T> collectionElementClass) {
        Converter[] result = new Converter[_collectionTypes.size()];
        int i = 0;

        for (Class<? extends Collection> iCollectionType : _collectionTypes) {
            result[i++] = new Converter(
                    getArrayClass(collectionElementClass),
                    iCollectionType,
                    (Object source) -> {
                        T[] sourceArray = (T[]) source;
                        Collection destination = createCollectoinInstance(iCollectionType);

                        Arrays.stream(sourceArray).forEach(destination::add);

                        return destination;
                    }
            );
        }

        return result;
    }

    private static Class getArrayClass(final Class elementType) {
        return Array.newInstance(elementType, 0).getClass();
    }

    private static Map<Class, Class<? extends Collection>> getDefaultCollectionImplementations() {
        Map<Class, Class<? extends Collection>> result = new HashMap<>();

        result.put(AbstractCollection.class, ArrayList.class);
        result.put(AbstractList.class, ArrayList.class);
        result.put(AbstractSequentialList.class, LinkedList.class);
        result.put(AbstractSet.class, HashSet.class);
        result.put(Collection.class, ArrayList.class);
        result.put(List.class, ArrayList.class);
        result.put(NavigableSet.class, TreeSet.class);
        result.put(Set.class, HashSet.class);
        result.put(SortedSet.class, TreeSet.class);

        return result;
    }

    private static Collection<Class<? extends Collection>> getCollectionTypes() {
        Collection<Class<? extends Collection>> result = new HashSet<>();

        // All classes and interfaces in java.util and java.util.concurent packages inherited
        // from Collection excluding queues and deques
        result.add(List.class);
        result.add(NavigableSet.class);
        result.add(Set.class);
        result.add(SortedSet.class);
        result.add(AbstractCollection.class);
        result.add(AbstractList.class);
        result.add(AbstractSequentialList.class);
        result.add(AbstractSet.class);
        result.add(ArrayList.class);
        result.add(Collection.class);
        result.add(ConcurrentSkipListSet.class);
        result.add(CopyOnWriteArrayList.class);
        result.add(CopyOnWriteArraySet.class);
        result.add(HashSet.class);
        result.add(LinkedHashSet.class);
        result.add(LinkedList.class);
        result.add(Stack.class);
        result.add(TreeSet.class);
        result.add(Vector.class);

        return result;
    }

    private static Converter[] buildCollectionToCollectionConverters(
            final Collection<Class<? extends Collection>> collectionTypes) {
        Converter[] result = new Converter[collectionTypes.size()];
        int i = 0;

        for (Class<? extends Collection> iCollectionType : collectionTypes) {
            result[i++] = new Converter(
                    Collection.class,
                    iCollectionType,
                    (Object source) -> {
                        Collection sourceCollection = (Collection) source;
                        Collection destination = createCollectoinInstance(iCollectionType);

                        destination.addAll(sourceCollection);

                        return destination;
                    }
            );
        }

        return result;
    }

    private static Collection createCollectoinInstance(
            final Class<? extends Collection> collectionType) {
        try {
            if (collectionType.isInterface() || Modifier.isAbstract(collectionType.getModifiers())) {
                Class<? extends Collection> implementationType
                        = _defaultCollectionImplementations.get(collectionType);

                if (implementationType == null) {
                    throw new MappingException(String.format(
                            "I don't know which implementation of %s use.",
                            collectionType));
                }

                return implementationType.newInstance();
            } else {
                return collectionType.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException(
                    String.format("Failed to create instance of %s class.", collectionType));
        }
    }
}
