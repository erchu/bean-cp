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

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(Source.class, Destination.class, (source, destination) -> {
                    destination.setXySum(source.getX() + source.getY());
                })
                .buildMapper();

        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals("Invalid mapping result.", 3 + 7, result.getXySum());
    }

    @Test
    public void mapper_should_allow_define_destination_object_builder_for_converter() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(3);
        sourceInstance.setY(7);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(Source.class, Destination.class, (source, destination) -> {
                    destination.setXySum(source.getX() + source.getY());
                }, () -> {
                    Destination destination = new Destination();
                    destination.setFlag(8);

                    return destination;
                })
                .buildMapper();

        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals("Invalid mapping result.", 3 + 7, result.getXySum());
        assertEquals("Destination object not constructed by builder?", 8, result.getFlag());
    }

    @Test
    public void mapper_should_allow_to_map_inner_classes_by_mapper() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setX(34);

        SourceOuter sourceOuterInstance = new SourceOuter();
        sourceOuterInstance.setInner(sourceInstance);
        sourceOuterInstance.setZ(-22);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(SourceOuter.class, DestinationOuter.class,
                        (mapperRef, source, destination) -> {
                            destination.setC(source.getZ());

                            if (destination.getInner() == null) {
                                destination.setInner(mapperRef.map(source.getInner(), Destination.class));
                            } else {
                                mapperRef.map(source.getInner(), destination.getInner());
                            }
                        })
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.bind(source::getX, destination::setFlag)
                )
                .buildMapper();

        DestinationOuter result = mapper.map(sourceOuterInstance, DestinationOuter.class);

        // THEN
        assertEquals("Invalid 'c' value.", sourceOuterInstance.getZ(), result.getC());
        assertNotNull("Invalid 'getInner()' value.", result.getInner());
        assertEquals("Invalid 'getInner().getFlag()' value.", sourceInstance.getX(), result.getInner().getFlag());
    }
}
