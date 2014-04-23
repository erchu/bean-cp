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

import org.beancp.commons.NameBasedMappingConvention;
import org.junit.Test;

public class OrderOfMapStatemetsTest {

    public static class Source {

        private String x, y;

        private InnerSource inner = new InnerSource();

        public InnerSource getInner() {
            return inner;
        }

        public void setInner(InnerSource inner) {
            this.inner = inner;
        }

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

        private String a, b, c;

        private InnerDestination inner = new InnerDestination();

        public InnerDestination getInner() {
            return inner;
        }

        public void setInner(InnerDestination inner) {
            this.inner = inner;
        }

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

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

    public static class InnerSource {
    }

    public static class InnerDestination {
    }

    @Test(expected = MapperConfigurationException.class)
    public void beforeMap_line_must_be_beforeConstant_bind_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bindConstant("1", destination::setA)
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void beforeMap_line_must_be_before_bind_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getY, destination::setB)
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void beforeMap_line_must_be_before_useConvention_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void beforeMap_line_must_be_before_mapInner_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(InnerSource.class, InnerDestination.class, (config, source, destination) -> {})
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .mapInner(source::getInner, destination::setInner, InnerDestination.class)
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void afterMap_line_must_be_after_bind_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void afterMap_line_must_be_after_bindConstant_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void afterMap_line_must_be_after_mapInner_lines() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(InnerSource.class, InnerDestination.class, (config, source, destination) -> {})
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .mapInner(source::getInner, destination::setInner, InnerDestination.class));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void afterMap_line_must_be_after_useConvention_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .useConvention(NameBasedMappingConvention.getStrictMatch()));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void useConvention_line_must_be_before_bind_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bind(source::getY, destination::setB)
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void useConvention_line_must_be_beforeConstant_bind_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .bindConstant("1", destination::setA)
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void useConvention_line_must_be_before_mapInner_line() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(InnerSource.class, InnerDestination.class, (config, source, destination) -> {})
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .mapInner(source::getInner, destination::setInner, InnerDestination.class)
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB())));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void useConvention_can_be_executed_only_once() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .useConvention(NameBasedMappingConvention.getStrictMatch()));

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void constructDestinationObjectUsing_can_be_executed_only_once() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .constructDestinationObjectUsing(() -> new Destination())
                        .constructDestinationObjectUsing(() -> new Destination()));

        // THEN: expect exception
    }

    @Test
    public void when_order_is_valid_then_should_be_no_error() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .constructDestinationObjectUsing(() -> new Destination())
                        .beforeMap(() -> destination.setC(destination.getA() + destination.getB()))
                        .useConvention(NameBasedMappingConvention.getStrictMatch())
                        .bindConstant("1", destination::setA)
                        .bind(source::getY, destination::setB)
                        .afterMap(() -> destination.setC(destination.getA() + destination.getB()))
                );

        // THEN: expect exception
    }
}
