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

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import static org.junit.Assert.*;
import org.junit.Test;

public class NameBasedConventionBasicScenarioTest {

    public static class SourceWithByte {

        private byte x;

        public byte getX() {
            return x;
        }

        public void setX(byte x) {
            this.x = x;
        }
    }

    public static class DestinationWithDouble {

        private double x;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }
    }

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

    public static class InheritanceTestBaseSource {

        private int x;

        public int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }

    public static class InheritanceTestSource extends InheritanceTestBaseSource {

        private int a;

        public int b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }
    }

    public static class InheritanceTestBaseDestination {

        private int a;

        public int b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }
    }

    public static class InheritanceTestDestination extends InheritanceTestBaseDestination {

        private int x;

        public int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
    
    public static class SourceWithStaticField {

        private int x;
        
        public static long y;
        
        public final long z = 1;
        
        public static final long serialVersionUID = 1L;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
    
    public static class SourceWithoutStaticField {

        private int x;
        
        public long y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
    
    public static class DestinationWithStaticField {

        private int x;
        
        public static long y;
        
        public final long z = 2;
        
        private static final long serialVersionUID = 2L;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
    
    public static class DestinationWithoutStaticField {

        private int x;
        
        public long y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }

    @Test
    public void when_source_and_destination_classes_has_properties_of_the_same_name_and_type_then_should_be_mapped() {
        // GIVEN
        SimpleSourceWithProperties sourceInstance = new SimpleSourceWithProperties();
        sourceInstance.setA(7);
        sourceInstance.setX(8);
        sourceInstance.setY(9);
        sourceInstance.setZ(10);
        sourceInstance.setInner(new InnerClass("hello"));

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SimpleSourceWithProperties.class, SimpleDestinationWithProperties.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

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
    public void when_source_and_destination_classes_has_fields_of_the_same_name_and_type_then_should_be_mapped() {
        // GIVEN
        SimpleSourceWithFields sourceInstance = new SimpleSourceWithFields();
        sourceInstance.a = 7;
        sourceInstance.x = 8;
        sourceInstance.y = 9;
        sourceInstance.z = 10;
        sourceInstance.inner = new InnerClass("hello");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SimpleSourceWithFields.class, SimpleDestinationWithFields.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

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

    @Test
    public void convention_should_map_from_fields_to_properties() {
        // GIVEN
        SimpleSourceWithFields sourceInstance = new SimpleSourceWithFields();
        sourceInstance.a = 7;
        sourceInstance.x = 8;
        sourceInstance.y = 9;
        sourceInstance.z = 10;
        sourceInstance.inner = new InnerClass("hello");

        Mapper mapper = new MapperBuilder()
                .addMap(SimpleSourceWithFields.class, SimpleDestinationWithProperties.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        // WHEN
        SimpleDestinationWithProperties result = mapper.map(sourceInstance, SimpleDestinationWithProperties.class);

        // THEN
        // 'b' has no corresponding - it will be not mapped by convention
        assertEquals("Invalid 'b' property value.", 0, result.getB());

        // 'x' and 'y' should be mapped by convention
        assertEquals("Invalid 'x' property value.", sourceInstance.x, result.getX());
        assertEquals("Invalid 'y' property value.", sourceInstance.y, result.getY());
        assertNotNull("'inner' property is null", result.getInner());
        assertEquals("Invalid 'inner' property value.", sourceInstance.inner.getValue(), result.getInner().getValue());

        // 'z' exists in source and in destination, but have different data types - it will be not
        // mapped by StrictMatch convention
        assertEquals("Invalid 'z' property value.", 0, result.getZ());
    }

    @Test
    public void convention_should_map_from_properties_to_fields() {
        // GIVEN
        SimpleSourceWithProperties sourceInstance = new SimpleSourceWithProperties();
        sourceInstance.setA(7);
        sourceInstance.setX(8);
        sourceInstance.setY(9);
        sourceInstance.setZ(10);
        sourceInstance.setInner(new InnerClass("hello"));

        Mapper mapper = new MapperBuilder()
                .addMap(SimpleSourceWithProperties.class, SimpleDestinationWithFields.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        // WHEN
        SimpleDestinationWithFields result = mapper.map(sourceInstance, SimpleDestinationWithFields.class);

        // THEN
        // 'b' has no corresponding - it will be not mapped by convention
        assertEquals("Invalid 'b' field value.", 0, result.b);

        // 'x' and 'y' should be mapped by convention
        assertEquals("Invalid 'x' field value.", sourceInstance.getX(), result.x);
        assertEquals("Invalid 'y' field value.", sourceInstance.getY(), result.y);
        assertNotNull("'inner' field is null", result.inner);
        assertEquals(
                "Invalid 'inner' field value.",
                sourceInstance.getInner().getValue(),
                result.inner.getValue());

        // 'z' exists in source and in destination, but have different data types - it will be not
        // mapped by StrictMatch convention
        assertEquals("Invalid 'z' field value.", 0, result.z);
    }

    @Test
    public void convention_should_use_converter_if_available() {
        // GIVEN
        SourceWithByte sourceInstance = new SourceWithByte();
        sourceInstance.setX((byte) 8);

        Mapper mapper = new MapperBuilder()
                .addConverter(byte.class, double.class, source -> (double) source)
                .addMap(SourceWithByte.class, DestinationWithDouble.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get()))
                .buildMapper();

        // WHEN
        DestinationWithDouble result = mapper.map(sourceInstance, DestinationWithDouble.class);

        // THEN
        assertEquals(8.0, result.getX(), 0.0);
    }

    @Test
    public void convention_should_match_inherited_members() {
        // GIVEN
        InheritanceTestSource sourceInstance = new InheritanceTestSource();
        sourceInstance.setA(3);
        sourceInstance.b = 1;
        sourceInstance.setX(6);
        sourceInstance.y = 2;

        Mapper mapper = new MapperBuilder()
                .addMap(InheritanceTestSource.class, InheritanceTestDestination.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        // WHEN
        // Result _base_ class has the same members that source _inherited_ class and vice versa
        InheritanceTestDestination result = mapper.map(sourceInstance, InheritanceTestDestination.class);

        // THEN
        assertEquals("Invalid a value", sourceInstance.getA(), result.getA());
        assertEquals("Invalid b value", sourceInstance.b, result.b);
        assertEquals("Invalid x value", sourceInstance.getX(), result.getX());
        assertEquals("Invalid y value", sourceInstance.y, result.y);
    }

    @Test
    public void static_nor_final_field_cannot_be_mapped() {
        // GIVEN
        SourceWithStaticField sourceInstance = new SourceWithStaticField();
        sourceInstance.setX(123);
        
        final int initialYValueAtSource = 234;
        SourceWithStaticField.y = initialYValueAtSource;
        
        final int initialYValueAtDestination = -initialYValueAtSource;
        DestinationWithStaticField.y = initialYValueAtDestination;

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithStaticField.class, DestinationWithStaticField.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        DestinationWithStaticField result = mapper.map(sourceInstance, DestinationWithStaticField.class);

        // THEN
        // 'x' should be mapped by convention
        assertEquals("Invalid 'x' property value.", sourceInstance.getX(), result.getX());
        
        // 'y' is static, so cannot be mapped
        assertEquals("Invalid 'y' property value.", initialYValueAtDestination, result.y);
    }

    @Test
    public void static_field_at_source_cannot_be_mapped() {
        // GIVEN
        SourceWithoutStaticField sourceInstance = new SourceWithoutStaticField();
        sourceInstance.setX(123);
        
        final int initialYValueAtSource = 234;
        sourceInstance.y = initialYValueAtSource;
        
        final int initialYValueAtDestination = -initialYValueAtSource;
        DestinationWithStaticField.y = initialYValueAtDestination;

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithoutStaticField.class, DestinationWithStaticField.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        DestinationWithStaticField result = mapper.map(sourceInstance, DestinationWithStaticField.class);

        // THEN
        // 'x' should be mapped by convention
        assertEquals("Invalid 'x' property value.", sourceInstance.getX(), result.getX());
        
        // 'y' is static, so cannot be mapped
        assertEquals("Invalid 'y' property value.", initialYValueAtDestination, result.y);
    }

    @Test
    public void static_field_at_destination_cannot_be_mapped() {
        // GIVEN
        SourceWithStaticField sourceInstance = new SourceWithStaticField();
        sourceInstance.setX(123);
        
        final int initialYValueAtSource = 234;
        SourceWithStaticField.y = initialYValueAtSource;
        
        final int initialYValueAtDestination = -initialYValueAtSource;
        DestinationWithoutStaticField result = new DestinationWithoutStaticField();
        result.y = initialYValueAtDestination;

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithStaticField.class, DestinationWithoutStaticField.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())
                ).buildMapper();

        mapper.map(sourceInstance, result);

        // THEN
        // 'x' should be mapped by convention
        assertEquals("Invalid 'x' property value.", sourceInstance.getX(), result.getX());
        
        // 'y' is static, so cannot be mapped
        assertEquals("Invalid 'y' property value.", initialYValueAtDestination, result.y);
    }
}
