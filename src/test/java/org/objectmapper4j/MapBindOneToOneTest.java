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

public class MapBindOneToOneTest {

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

    public static class SourceInnerSource {

        private SourceWithProperties innerSource;

        public SourceWithProperties getInnerSource() {
            return innerSource;
        }

        public void setInnerSource(SourceWithProperties innerSource) {
            this.innerSource = innerSource;
        }
    }

    public static class AnotherSourceWithInnerSource {

        private SourceInnerSource innerSource;

        public SourceInnerSource getInnerSource() {
            return innerSource;
        }

        public void setInnerSource(SourceInnerSource innerSource) {
            this.innerSource = innerSource;
        }
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_source_expression() {
        new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, ref) -> config
                        .bindOneToOne(null, ref.destination()::setA));
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, ref) -> config
                        .bindOneToOne(ref.source()::getX, null));
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
                        (config, ref) -> config
                        .bindOneToOne(() -> ref.source().getX(), ref.destination()::setA)
                        .bindOneToOne(() -> ref.source().getY(), ref.destination()::setB))
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
                        (config, ref) -> config
                        .bindOneToOne(() -> ref.source().x, v -> ref.destination().a = v)
                        .bindOneToOne(() -> ref.source().y, v -> ref.destination().b = v))
                .buildMapper();

        DestinationWithFields result = mapper.map(sampleSource, DestinationWithFields.class);

        // THEN
        assertEquals(
                "Property 'x' is not mapped correctly.", "xval", result.a);
        assertEquals(
                "Property 'y' is not mapped correctly.", "yval", result.b);
    }

    @Test
    public void mapper_should_be_able_to_bind_inner_classes() {
        // GIVEN
        AnotherSourceWithInnerSource sourceInstance = new AnotherSourceWithInnerSource();

        sourceInstance.setInnerSource(new SourceInnerSource());
        sourceInstance.getInnerSource().setInnerSource(new SourceWithProperties());

        sourceInstance.getInnerSource().getInnerSource().setX("xval");
        sourceInstance.getInnerSource().getInnerSource().setY("yval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(AnotherSourceWithInnerSource.class, DestinationWithProperties.class,
                        (config, ref) -> config
                        .bindOneToOne(
                                () -> ref.source().getInnerSource().getInnerSource().getX(),
                                ref.destination()::setA)
                        .bindOneToOne(
                                () -> ref.source().getInnerSource().getInnerSource().getY(),
                                ref.destination()::setB))
                .buildMapper();

        DestinationWithProperties destination = mapper.map(sourceInstance, DestinationWithProperties.class);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", destination.getA());
        assertEquals("Property 'y' is not mapped correctly.", "yval", destination.getB());
    }
}
