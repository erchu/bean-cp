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
public class MapBindTest {

    public static class SourceWithProperties {

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

    public static class DestinationWithProperties {

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

    public static class SourceWithFields {

        public String x;

        public String y;
    }

    public static class DestinationWithFields {

        public String a;

        private String b;
    }

    public static final class FinalSource {

        private String x;

        private FinalSource(final String x) {
            this.x = x;
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }
    }

    public static final class FinalDestination {

        private String a;

        public FinalDestination(final String a) {
            this.a = a;
        }

        public final String getA() {
            return a;
        }

        public final void setA(String a) {
            this.a = a;
        }
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_source_expression() {
        new MapperBuilder()
                .addMap(new Map<SourceWithProperties, DestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SourceWithProperties source,
                            final DestinationWithProperties destination) {
                                this.<String>bind(null, destination::setA);
                            }
                });
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(new Map<SourceWithProperties, DestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SourceWithProperties source,
                            final DestinationWithProperties destination) {
                                this.<String>bind(source::getX, null);
                            }
                });
    }

    @Test
    public void mapper_should_bind_properties() {
        // GIVEN
        SourceWithProperties source = new SourceWithProperties();
        source.setX("xval");
        source.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SourceWithProperties, DestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SourceWithProperties source,
                            final DestinationWithProperties destination) {
                                this.<String>bind(source::getX, destination::setA);
                                this.<String>bind(source::getY, destination::setB);
                            }
                })
                .buildMapper();

        DestinationWithProperties destination = mapper.map(source, DestinationWithProperties.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
        assertEquals("Property 'y' is not mapped correctly.", "yval", destination.getB());
    }

    @Test
    public void mapper_should_bind_fields() {
        // GIVEN
        SourceWithFields source = new SourceWithFields();
        source.x = "xval";
        source.y = "yval";

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SourceWithFields, DestinationWithFields>() {

                    @Override
                    public void configure(
                            final SourceWithFields source,
                            final DestinationWithFields destination) {
                                this.<String>bind(() -> source.x, v -> destination.a = v);
                                this.<String>bind(() -> source.y, v -> destination.b = v);
                            }
                })
                .buildMapper();

        DestinationWithFields destination = mapper.map(source, DestinationWithFields.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.a);
        assertEquals("Property 'y' is not mapped correctly.", "yval", destination.b);
    }

    @Test
    public void mapper_should_bind_calculated_values() {
        // GIVEN
        SourceWithProperties source = new SourceWithProperties();
        source.setX("xval");
        source.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SourceWithProperties, DestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SourceWithProperties source,
                            final DestinationWithProperties destination) {
                                this.<String>bind(() -> source.getX() + source.getY(), destination::setA);
                                this.<String>bind(() -> source.getY() + "2", destination::setB);
                            }
                })
                .buildMapper();

        DestinationWithProperties destination = mapper.map(source, DestinationWithProperties.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xvalyval", destination.getA());
        assertEquals("Property 'y' is not mapped correctly.", "yval2", destination.getB());
    }
}
