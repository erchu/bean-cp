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

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_source_expression() {
        new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, source, destination) -> config
                        .bind(null, destination::setA));
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, source, destination) -> config
                        .bind(source::getX, null));
    }

    @Test
    public void mapper_should_bind_properties() {
        // GIVEN
        SourceWithProperties sampleSource = new SourceWithProperties();
        sampleSource.setX("xval");
        sampleSource.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB))
                .buildMapper();

        DestinationWithProperties result = mapper.map(sampleSource, DestinationWithProperties.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
        assertEquals("Property 'y' is not mapped correctly.", "yval", result.getB());
    }

    @Test
    public void mapper_should_bind_fields() {
        // GIVEN
        SourceWithFields sampleSource = new SourceWithFields();
        sampleSource.x = "xval";
        sampleSource.y = "yval";

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithFields.class, DestinationWithFields.class,
                        (config, source, destination) -> config
                        .bind(() -> source.x, v -> destination.a = v)
                        .bind(() -> source.y, v -> destination.b = v))
                .buildMapper();

        DestinationWithFields result = mapper.map(sampleSource, DestinationWithFields.class);

        // THEN
        assertEquals(
                "Property 'x' is not mapped correctly.", "xval", result.a);
        assertEquals(
                "Property 'y' is not mapped correctly.", "yval", result.b);
    }

    @Test
    public void mapper_should_bind_calculated_values() {
        // GIVEN
        SourceWithProperties sourceInstance = new SourceWithProperties();
        sourceInstance.setX("xval");
        sourceInstance.setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, source, destination) -> config
                        .bind(() -> source.getX() + source.getY(), destination::setA)
                        .bind(() -> source.getY() + "2", destination::setB))
                .buildMapper();

        DestinationWithProperties destination = mapper.map(sourceInstance, DestinationWithProperties.class);

        // THEN
        assertEquals(
                "Property 'x' is not mapped correctly.", "xvalyval", destination.getA());
        assertEquals(
                "Property 'y' is not mapped correctly.", "yval2", destination.getB());
    }
}
