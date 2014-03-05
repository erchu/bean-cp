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

import org.junit.Test;
import static org.junit.Assert.*;

//TODO: Test is not complete, only basic scenario (proof of concept) implemented
public class MapBindConstantTest {

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

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(new Map<Source, Destination>() {

                    @Override
                    public void configure(final Source source, final Destination destination) {
                        this.<String>bindConstant("const", null);
                    }
                });
    }

    @Test
    public void mapper_should_be_able_to_bind_to_constants() {
        // GIVEN
        Source source = new Source();
        source.setX("xval");
        source.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<Source, Destination>() {

                    @Override
                    public void configure(final Source source, final Destination destination) {
                        this.<String>bindConstant("const", destination::setA);
                    }
                })
                .buildMapper();

        Destination destination = mapper.map(source, Destination.class);

        // THEN
        assertEquals("Destination property 'a' is not mapped correctly.", "const", destination.getA());
        assertNull("Destination property 'b' is not mapped correctly.", destination.getB());
    }

    @Test
    public void mapper_should_allow_null_as_source() {
        // GIVEN
        Source source = new Source();
        source.setX("xval");
        source.setY("yval");

        Destination destination = new Destination();
        destination.setA("aval");
        destination.setB("bval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<Source, Destination>() {

                    @Override
                    public void configure(final Source source, final Destination destination) {
                        this.<String>bindConstant(null, destination::setA);
                    }
                })
                .buildMapper();

        mapper.map(source, destination);

        // THEN
        assertNull("Destination property 'a' is not mapped correctly.", destination.getA());
        assertEquals("Destination property 'b' is not mapped correctly.", "bval", destination.getB());
    }
}
