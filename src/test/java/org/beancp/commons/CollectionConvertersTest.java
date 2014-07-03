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
package org.beancp.commons;

import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionConvertersTest {

    private static class T implements Comparable {

        private final int id;

        public T(final int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            final T other = (T) obj;

            return (this.id == other.id);
        }

        @Override
        public int compareTo(final Object o) {
            T other = (T) o;

            return Integer.compare(this.id, other.id);
        }
    }

    @Test
    public void should_be_able_to_map_any_array_to_any_collection() {
        // GIVEN
        T[] sourceInstance = new T[] { new T(1), new T(2), new T(3) };
        Collection<Class<? extends Collection>> collectionTypes
                = getCollectionTypes()
                .stream()
                .filter(i
                        -> i.isInterface() == false
                        && Modifier.isAbstract(i.getModifiers()) == false)
                .collect(Collectors.toList());

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.getArrayToCollection(T.class))
                .buildMapper();

        for (Class<? extends Collection> i : collectionTypes) {
            Collection result = mapper.map(sourceInstance, i);

            // THEN
            assertEquals(
                    "Invalid result size for " + i,
                    sourceInstance.length,
                    result.size());

            for (T j : sourceInstance) {
                assertTrue(
                        "Missing element " + j.getId() + " in collection " + i,
                        result.contains(j));
            }
        }
    }

    @Test
    public void should_be_able_to_map_any_collection_to_array()
            throws InstantiationException, IllegalAccessException {
        // GIVEN
        T[] testValues = new T[] { new T(1), new T(2), new T(3) };
        Iterable<Class<? extends Collection>> collectionTypes
                = getCollectionTypes();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.getCollectionToArray(T.class))
                .buildMapper();

        for (Class<? extends Collection> sourceCollection : collectionTypes) {
            if (sourceCollection.isInterface() || Modifier.isAbstract(sourceCollection.getModifiers())) {
                continue;
            }
            
            Collection collectionInstance = sourceCollection.newInstance();

            Arrays.stream(testValues).forEach(collectionInstance::add);

            T[] result = mapper.map(collectionInstance, T[].class);

            // THEN
            assertEquals("Invalid result size for " + sourceCollection, testValues.length, result.length);

            for (T j : testValues) {
                assertTrue(
                        "Missing element " + j.getId() + " in collection " + sourceCollection,
                        Arrays.stream(result).anyMatch(k -> k.equals(j)));
            }
        }
    }

    @Test
    public void should_be_able_to_map_any_collection_to_any_collection()
            throws InstantiationException, IllegalAccessException {
        // GIVEN
        T[] testValues = new T[] { new T(1), new T(2), new T(3) };
        Iterable<Class<? extends Collection>> collectionTypes
                = getCollectionTypes();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.get())
                .buildMapper();

        for (Class<? extends Collection> sourceCollection : collectionTypes) {
            if (sourceCollection.isInterface() || Modifier.isAbstract(sourceCollection.getModifiers())) {
                continue;
            }

            Collection collectionInstance = sourceCollection.newInstance();

            Arrays.stream(testValues).forEach(collectionInstance::add);

            for (Class<? extends Collection> destinationCollection : collectionTypes) {
                Collection<T> result = mapper.map(collectionInstance, destinationCollection);

                // THEN
                assertEquals(
                        "Invalid result size for "
                        + sourceCollection
                        + " to "
                        + destinationCollection
                        + " conversion",
                        testValues.length,
                        result.size());

                for (T i : testValues) {
                    assertTrue(
                            "Missing element "
                            + i.getId()
                            + " in collection "
                            + destinationCollection
                            + " made from "
                            + sourceCollection,
                            result.contains(i));
                }
            }
        }
    }

    @Test
    public void should_be_able_to_map_array_of_primitive_type_to_collection() {
        // GIVEN
        long[] sourceInstance = new long[] { 1, 2, 3 };

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.getArrayToCollection(long.class))
                .buildMapper();

        Collection result = mapper.map(sourceInstance, Collection.class);

        // THEN
        assertEquals(
                "Invalid result size",
                sourceInstance.length,
                result.size());

        for (long j : sourceInstance) {
            assertTrue(
                    "Missing element " + j + " in collection",
                    result.contains(j));
        }
    }

    @Test
    public void should_be_able_to_map_any_collection_to_array_of_primitive_type() {
        // GIVEN
        long[] testValues = new long[] { 1, 2, 3 };

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.getCollectionToArray(long.class))
                .buildMapper();

        Collection<Long> collectionInstance = new LinkedList<>();

        Arrays.stream(testValues).forEach(collectionInstance::add);

        long[] result = mapper.map(collectionInstance, long[].class);

        // THEN
        assertEquals("Invalid result size", testValues.length, result.length);

        for (long j : testValues) {
            assertTrue(
                    "Missing element " + j + " in collection",
                    Arrays.stream(result).anyMatch(k -> k == j));
        }
    }

    private Collection<Class<? extends Collection>> getCollectionTypes() {
        Collection<Class<? extends Collection>> result = new HashSet<>();

        result.add(List.class);
        result.add(NavigableSet.class);
        result.add(Set.class);
        result.add(SortedSet.class);
        result.add(AbstractCollection.class);
        result.add(AbstractList.class);
        result.add(AbstractSequentialList.class);
        result.add(AbstractSet.class);
        result.add(ArrayList.class);
        result.add(ConcurrentSkipListSet.class);
        result.add(CopyOnWriteArrayList.class);
        result.add(CopyOnWriteArraySet.class);
        //result.add(EnumSet.class); // Specialized collection excluded in test
        result.add(HashSet.class);
        result.add(LinkedHashSet.class);
        result.add(LinkedList.class);
        result.add(Stack.class);
        result.add(TreeSet.class);
        result.add(Vector.class);

        return result;
    }
}
