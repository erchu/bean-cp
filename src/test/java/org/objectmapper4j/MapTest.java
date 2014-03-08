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

    public static class RestrictedSource {

        private String x;

        protected RestrictedSource() {
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }
    }

    public static class RestrictedDestination {

        private String a;

        protected RestrictedDestination() {
        }

        public final String getA() {
            return a;
        }

        public final void setA(String a) {
            this.a = a;
        }
    }

    @Test
    public void mapper_should_work_even_classes_default_constructor_is_protected_and_mapped_properties_are_final()
            throws NoSuchFieldException {
        // GIVEN
        RestrictedSource sampleSource = new RestrictedSource();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(RestrictedSource.class, RestrictedDestination.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        RestrictedDestination result = new RestrictedDestination();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
    }
}
