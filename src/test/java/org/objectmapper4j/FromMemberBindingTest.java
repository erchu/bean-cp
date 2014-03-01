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
public class FromMemberBindingTest {

    public static class SimpleSourceWithProperties {

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

    public static class SimpleDestinationWithProperties {

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

    public static class SimpleSourceWithFields {

        public String x;

        public String y;
    }

    public static class SimpleDestinationWithFields {

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

    @Test
    public void mapper_should_be_able_to_extract_from_property_binding_information() {
        // GIVEN
        SimpleSourceWithProperties source = new SimpleSourceWithProperties();
        source.setX("xval");
        source.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SimpleSourceWithProperties, SimpleDestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SimpleSourceWithProperties source,
                            final SimpleDestinationWithProperties destination) {
                                this.<String>bind(source::getX, destination::setA);
                                this.<String>bind(source::getY, destination::setB);
                            }
                })
                .buildMapper();

        SimpleDestinationWithProperties destination
                = mapper.map(source, SimpleDestinationWithProperties.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
        assertEquals("Property 'y' is not mapped correctly.", "yval", destination.getB());
    }

    @Test
    public void mapper_should_be_able_to_bind_to_constants() {
        // GIVEN
        SimpleSourceWithProperties source = new SimpleSourceWithProperties();
        source.setX("xval");
        source.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SimpleSourceWithProperties, SimpleDestinationWithProperties>() {

                    @Override
                    public void configure(
                            final SimpleSourceWithProperties source,
                            final SimpleDestinationWithProperties destination) {
                                this.<String>bindConstant("const", destination::setA);
                            }
                })
                .buildMapper();

        SimpleDestinationWithProperties destination
                = mapper.map(source, SimpleDestinationWithProperties.class);

        // THEN
        assertEquals("Destination property 'a' is not mapped correctly.", "const", destination.getA());
        assertNull("Destination property 'b' is not mapped correctly.", destination.getB());
    }

    @Test
    public void mapper_should_be_able_to_extract_from_field_binding_information() {
        // GIVEN
        SimpleSourceWithFields source = new SimpleSourceWithFields();
        source.x = "xval";
        source.y = "yval";

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<SimpleSourceWithFields, SimpleDestinationWithFields>() {

                    @Override
                    public void configure(
                            final SimpleSourceWithFields source,
                            final SimpleDestinationWithFields destination) {
                                this.<String>bind(() -> source.x, v -> destination.a = v);
                                this.<String>bind(() -> source.y, v -> destination.b = v);
                            }
                })
                .buildMapper();

        SimpleDestinationWithFields destination
                = mapper.map(source, SimpleDestinationWithFields.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.a);
        assertEquals("Property 'y' is not mapped correctly.", "yval", destination.b);
    }

    @Test
    public void mapper_should_work_even_for_final_classes_and_final_class_members_with_no_default_constructor()
            throws NoSuchFieldException {
        // GIVEN
        FinalSource source = new FinalSource("");
        source.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<FinalSource, FinalDestination>() {

                    @Override
                    public void configure(
                            final FinalSource source,
                            final FinalDestination destination) {
                                this.<String>bind(source::getX, destination::setA);
                            }
                })
                .buildMapper();

        FinalDestination destination = new FinalDestination("");
        mapper.map(source, destination);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
    }
}
