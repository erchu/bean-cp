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

public class BindConstantTest {

    public static class Source {

        private String x;

        private String y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }

    public static class Destination {

        private String a;

        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

    @Test(expected = NullPointerException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bindConstant("const", null));
    }

    @Test
    public void mapper_should_be_able_to_bind_to_constants() {
        // GIVEN
        Source sampleSource = new Source();
        sampleSource.setX("xval");
        sampleSource.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class,
                        (config, source, destination) -> config
                        .bindConstant("const", destination::setA))
                .buildMapper();

        Destination result = mapper.map(sampleSource, Destination.class);

        // THEN
        assertEquals("Destination property 'a' is not mapped correctly.", "const", result.getA());
        assertNull("Destination property 'b' is not mapped correctly.", result.getB());
    }

    @Test
    public void mapper_should_allow_null_as_source() {
        // GIVEN
        Source sampleSource = new Source();
        sampleSource.setX("xval");
        sampleSource.setY("yval");

        Destination result = new Destination();
        result.setA("aval");
        result.setB("bval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bindConstant(null, destination::setA))
                .buildMapper();

        mapper.map(sampleSource, result);

        // THEN
        assertNull("Destination property 'a' is not mapped correctly.", result.getA());
        assertEquals("Destination property 'b' is not mapped correctly.", "bval", result.getB());
    }
}
