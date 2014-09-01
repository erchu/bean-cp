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

import com.github.erchu.beancp.commons.NameBasedMapConvention;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class MapAnyByConventionTest {

    public static class InnerClass {

        private final String value;

        public InnerClass(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class SimpleSourceWithProperties {

        private int x, y;

        private int z;

        private int a;

        private InnerClass inner;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public InnerClass getInner() {
            return inner;
        }

        public void setInner(InnerClass inner) {
            this.inner = inner;
        }
    }

    public static class SimpleDestinationWithProperties {

        private int x, y;

        private long z;

        private int b;

        private InnerClass inner;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public long getZ() {
            return z;
        }

        public void setZ(long z) {
            this.z = z;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public InnerClass getInner() {
            return inner;
        }

        public void setInner(InnerClass inner) {
            this.inner = inner;
        }
    }

    public static class SimpleSourceWithFields {

        public int x, y;

        public int z;

        public int a;

        public InnerClass inner;
    }

    public static class SimpleDestinationWithFields {

        public int x, y;

        public long z;

        public int b;

        public InnerClass inner;
    }

    @Test
    public void when_source_and_destination_classes_has_properties_of_the_same_name_and_type_then_should_be_mapped_also_when_it_is_not_declared_implicity() {
        // GIVEN
        SimpleSourceWithProperties sourceInstance = new SimpleSourceWithProperties();
        sourceInstance.setA(7);
        sourceInstance.setX(8);
        sourceInstance.setY(9);
        sourceInstance.setZ(10);
        sourceInstance.setInner(new InnerClass("hello"));

        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMapConvention.get())
                .buildMapper();

        // WHEN
        SimpleDestinationWithProperties result = mapper.map(sourceInstance, SimpleDestinationWithProperties.class);

        // THEN
        // 'b' has no corresponding - it will be not mapped by convention
        assertEquals("Invalid 'b' property value.", 0, result.getB());

        // 'x' and 'y' should be mapped by convention
        assertEquals("Invalid 'x' property value.", sourceInstance.getX(), result.getX());
        assertEquals("Invalid 'y' property value.", sourceInstance.getY(), result.getY());
        assertNotNull("'inner' property is null", result.getInner());
        assertEquals("Invalid 'inner' property value.", sourceInstance.getInner().getValue(), result.getInner().getValue());

        // 'z' exists in source and in destination, but have different data types - it will be not
        // mapped by StrictMatch convention
        assertEquals("Invalid 'z' property value.", 0, result.getZ());
    }

    @Test
    public void when_source_and_destination_classes_has_fields_of_the_same_name_and_type_then_should_be_mapped_also_when_it_is_not_declared_implicity() {
        // GIVEN
        SimpleSourceWithFields sourceInstance = new SimpleSourceWithFields();
        sourceInstance.a = 7;
        sourceInstance.x = 8;
        sourceInstance.y = 9;
        sourceInstance.z = 10;
        sourceInstance.inner = new InnerClass("hello");

        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMapConvention.get())
                .buildMapper();

        // WHEN
        SimpleDestinationWithFields result = mapper.map(sourceInstance, SimpleDestinationWithFields.class);

        // THEN
        // 'b' has no corresponding - it will be not mapped by convention
        assertEquals("Invalid 'b' field value.", 0, result.b);

        // 'x' and 'y' should be mapped by convention
        assertEquals("Invalid 'x' field value.", sourceInstance.x, result.x);
        assertEquals("Invalid 'y' field value.", sourceInstance.y, result.y);
        assertNotNull("'inner' field is null", result.inner);
        assertEquals("Invalid 'inner' field value.", sourceInstance.inner.getValue(), result.inner.getValue());

        // 'z' exists in source and in destination, but have different data types - it will be not
        // mapped by StrictMatch convention
        assertEquals("Invalid 'z' field value.", 0, result.z);
    }
}
