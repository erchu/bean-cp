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

public class MapTest {

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

    public static final class RestrictedSource {

        private String x;

        private RestrictedSource(final String x) {
            this.x = x;
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }
    }

    public static final class RestrictedDestination {

        private String a;

        public RestrictedDestination(final String a) {
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
    public void mapper_should_work_even_for_final_classes_and_final_class_members_with_no_default_constructor()
            throws NoSuchFieldException {
        // GIVEN
        RestrictedSource source = new RestrictedSource("");
        source.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(new Map<RestrictedSource, RestrictedDestination>() {

                    @Override
                    public void configure(
                            final RestrictedSource source,
                            final RestrictedDestination destination) {
                                this.<String>bind(source::getX, destination::setA);
                            }
                })
                .buildMapper();

        RestrictedDestination destination = new RestrictedDestination("");
        mapper.map(source, destination);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
    }

    private static class SourceToDestinationMap extends Map<Source, Destination> {

        private boolean enableBindings;

        public boolean getEnableBindings() {
            return enableBindings;
        }

        public void setEnableBindings(boolean enableBindings) {
            this.enableBindings = enableBindings;
        }

        @Override
        public void configure(Source source, Destination destination) {
            if (enableBindings) {
                this.<String>bind(source::getX, destination::setA);
            }
        }
    }

    @Test
    public void map_definitions_after_change_should_not_affect_already_created_mappers()
            throws NoSuchFieldException {
        // GIVEN
        Source source = new Source();
        source.setX("xval");

        // WHEN
        SourceToDestinationMap map = new SourceToDestinationMap();
        map.setEnableBindings(true);

        Mapper mapper = new MapperBuilder()
                .addMap(map)
                .buildMapper();

        map.setEnableBindings(false);

        Destination destination = mapper.map(source, Destination.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
    }
}
