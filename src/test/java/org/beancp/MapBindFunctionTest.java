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
package org.beancp;

import org.beancp.MapperBuilder;
import org.beancp.NullParameterException;
import org.beancp.Mapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapBindFunctionTest {

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
                        .bindFunction(null, ref.destination()::setA));
    }

    @Test(expected = NullParameterException.class)
    public void mapper_should_not_allow_null_as_destination_expression() {
        new MapperBuilder()
                .addMap(SourceWithProperties.class, DestinationWithProperties.class,
                        (config, ref) -> config
                        .bindFunction(ref.source()::getX, null));
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
                        (config, ref) -> config
                        .bindFunction(() -> ref.source().getX() + ref.source().getY(), ref.destination()::setA)
                        .bindFunction(() -> ref.source().getY() + "2", ref.destination()::setB))
                .buildMapper();

        DestinationWithProperties destination = mapper.map(sourceInstance, DestinationWithProperties.class);

        // THEN
        assertEquals(
                "Property 'x' is not mapped correctly.", "xvalyval", destination.getA());
        assertEquals(
                "Property 'y' is not mapped correctly.", "yval2", destination.getB());
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
                        .bindFunction(
                                () -> {
                                    SourceWithProperties innerSource = ref.source()
                                    .getInnerSource().getInnerSource();

                                    return innerSource.getX() + innerSource.getY();
                                },
                                ref.destination()::setA)
                        .bindFunction(
                                () -> ref.source().getInnerSource().getInnerSource().getY() + "2",
                                ref.destination()::setB))
                .buildMapper();

        DestinationWithProperties destination = mapper.map(sourceInstance, DestinationWithProperties.class);

        // THEN
        assertEquals(
                "Property 'x' is not mapped correctly.", "xvalyval", destination.getA());
        assertEquals(
                "Property 'y' is not mapped correctly.", "yval2", destination.getB());
    }
}
