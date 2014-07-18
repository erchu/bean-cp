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

import com.github.erchu.beancp.MappingException;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.Mapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConverterTest {

    public static class Source {

        private int x;

        private int y;

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
    }

    public static class Destination {

        private int xySum;

        private int flag;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getXySum() {
            return xySum;
        }

        public void setXySum(int xySum) {
            this.xySum = xySum;
        }
    }

    public static class InheritedFromSource extends Source {
    }

    public static class InheritedFromDestination extends Destination {
    }

    public static class XY {

        private int x;

        private int y;

        private XY() {
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public static XY of(final int x) {
            XY result = new XY();
            result.x = x;
            result.y = 0;

            return result;
        }
    }

    public static class SourceOuter {

        private Source inner;

        private int z;

        public Source getInner() {
            return inner;
        }

        public void setInner(Source inner) {
            this.inner = inner;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

    public static class DestinationOuter {

        private Destination inner;

        private int c;

        public Destination getInner() {
            return inner;
        }

        public void setInner(Destination inner) {
            this.inner = inner;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }
    }

    @Test
    public void mapper_should_use_converter_if_available() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(3);
        sourceInstance.setY(7);

        Mapper mapper = new MapperBuilder()
                .addConverter(Source.class, Destination.class, (source) -> {
                    Destination destination = new Destination();

                    destination.setXySum(source.getX() + source.getY());
                    destination.setFlag(8);

                    return destination;
                })
                .buildMapper();

        // WHEN
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals("Invalid mapping result.", 3 + 7, result.getXySum());

        assertEquals(
                "Destination object not constructed by builder?",
                8, // constant set by converter
                result.getFlag());
    }

    @Test
    public void mapper_should_convert_inherited_source_classes() {
        // GIVEN
        Source sourceInstance = new InheritedFromSource();
        sourceInstance.setX(3);
        sourceInstance.setY(7);

        Mapper mapper = new MapperBuilder()
                .addConverter(Source.class, Destination.class, (source) -> {
                    Destination destination = new Destination();

                    destination.setXySum(source.getX() + source.getY());
                    destination.setFlag(8);

                    return destination;
                })
                .buildMapper();

        // WHEN
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals("Invalid mapping result.", 3 + 7, result.getXySum());

        assertEquals(
                "Destination object not constructed by builder?",
                8, // constant set by converter
                result.getFlag());
    }

    @Test(expected = MappingException.class)
    public void mapper_should_NOT_convert_to_inherited_destination_classes() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(3);
        sourceInstance.setY(7);

        Mapper mapper = new MapperBuilder()
                .addConverter(Source.class, Destination.class, (source) -> {
                    Destination destination = new Destination();

                    destination.setXySum(source.getX() + source.getY());
                    destination.setFlag(8);

                    return destination;
                })
                .buildMapper();

        // WHEN
        mapper.map(sourceInstance, InheritedFromDestination.class);

        // THEN: expect exception (map/converter not available)
    }

    @Test
    public void mapper_should_allow_to_map_inner_classes_by_mapper() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(34);

        SourceOuter sourceOuterInstance = new SourceOuter();
        sourceOuterInstance.setInner(sourceInstance);
        sourceOuterInstance.setZ(-22);
        Mapper mapper = new MapperBuilder()
                .addConverter(SourceOuter.class, DestinationOuter.class,
                        (mapperRef, source) -> {
                            DestinationOuter destination = new DestinationOuter();

                            destination.setC(source.getZ());

                            if (destination.getInner() == null) {
                                destination.setInner(mapperRef.map(source.getInner(), Destination.class));
                            } else {
                                mapperRef.map(source.getInner(), destination.getInner());
                            }

                            return destination;
                        })
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.bind(source::getX, destination::setFlag)
                )
                .buildMapper();

        // WHEN
        DestinationOuter result = mapper.map(sourceOuterInstance, DestinationOuter.class);

        // THEN
        assertEquals("Invalid 'c' value.", sourceOuterInstance.getZ(), result.getC());

        assertNotNull("Invalid 'getInner()' value.", result.getInner());

        assertEquals(
                "Invalid 'getInner().getFlag()' value.",
                sourceInstance.getX(),
                result.getInner().getFlag());
    }

    @Test
    public void converter_can_map_to_value_objects() {
        // GIVEN
        int sourceValue = 9;
        Integer sourceValueWrapper = sourceValue;

        Mapper mapper = new MapperBuilder()
                .addConverter(int.class, XY.class, source -> XY.of(source))
                .buildMapper();

        // WHEN
        XY resultFromPrimitiveType = mapper.map(sourceValue, XY.class);
        XY resultFromPrimitiveTypeWrapper = mapper.map(sourceValueWrapper, XY.class);

        // THEN
        assertEquals(
                "Invalid primitive type mapping result.",
                sourceValue,
                resultFromPrimitiveType.getX());

        assertEquals(
                "Invalid primitive type wrapper mapping result.",
                sourceValue,
                resultFromPrimitiveTypeWrapper.getX());
    }

    @Test
    public void converter_can_support_primitive_types_based_on_primitive_type_wrapper_converters() {
        // GIVEN
        int sourceValue = 9;

        Mapper mapper = new MapperBuilder()
                .addConverter(Integer.class, Double.class, source -> (double) source)
                .buildMapper();

        // WHEN
        double result = mapper.map(sourceValue, double.class);

        // THEN
        assertEquals("Invalid result.", (double) sourceValue, result, 0.0);
    }

    @Test
    public void converter_can_support_primitive_type_wrapper_based_on_primitive_types_converters() {
        // GIVEN
        Integer sourceValue = 9;

        Mapper mapper = new MapperBuilder()
                .addConverter(int.class, double.class, source -> new Double(source))
                .buildMapper();

        // WHEN
        Double result = mapper.map(sourceValue, Double.class);

        // THEN
        assertEquals("Invalid result.", sourceValue.doubleValue(), result, 0.0);
    }
}
