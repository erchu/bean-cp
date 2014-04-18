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

public class BindingOptionTest {

    public static class Source {

        private String x, y;

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

        private String a, b;

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

    @Test
    public void bind_SHOULD_be_executed_if_mapWhen_option_evaluates_to_TRUE() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY("8");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.mapWhen(() -> source.getX().length() == 1)) // is true
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "8", destinationObject.getB());  // value from source object
    }

    @Test
    public void bind_SHOULD_NOT_be_executed_if_mapWhen_option_evaluates_to_FALSE() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY("8");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.mapWhen(() -> source.getX().length() == 0)) // is false
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "borig", destinationObject.getB());  // value not mapped
    }

    @Test
    public void bindConstant_SHOULD_be_executed_if_mapWhen_option_evaluates_to_TRUE() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY("8");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bindConstant("yconst", destination::setB,
                                BindingOption.mapWhen(() -> source.getX().length() == 1)) // is true
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "yconst", destinationObject.getB());  // value from const
    }

    @Test
    public void bindConstant_SHOULD_NOT_be_executed_if_mapWhen_option_evaluates_to_FALSE() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY("8");

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bindConstant("yconst", destination::setB,
                                BindingOption.mapWhen(() -> source.getX().length() == 0)) // is false
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "borig", destinationObject.getB());  // value not mapped
    }

    @Test
    public void bind_from_null_SHOULD_replace_null_value_with_value_from_withNullSubstitution_option() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY(null);  // is null - will not be mapped

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.withNullSubstitution("XYZ"))
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "XYZ", destinationObject.getB());  // value from source object
    }

    @Test
    public void bind_from_null_SHOULD_NOT_null_value_with_value_if_withNullSubstitution_option_is_not_present() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY("8");  // is not null - will be mapped

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.withNullSubstitution("XYZ"))
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "8", destinationObject.getB());  // value from source object
    }

    @Test(expected = MapperConfigurationException.class)
    public void bindConstant_shold_not_accept_withNullSubstitution_option() {
        // GIVEN: source and destination class

        // WHEN
        new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bindConstant("C", destination::setB,
                                BindingOption.withNullSubstitution("XYZ"))
                );

        // THEN: expect exception
    }

    @Test
    public void when_mapWhen_option_evaluates_to_false_then_no_other_bind_options_should_be_evaluated() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY(null);  // is null

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.withNullSubstitution("XYZ"),  // null substitution enabled
                                BindingOption.mapWhen(() -> source.getX().length() == 0)) // is false
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "borig", destinationObject.getB());
    }

    @Test
    public void when_mapWhen_option_evaluates_to_true_then_other_bind_options_should_be_evaluated() {
        // GIVEN
        Source sourceObject = new Source();
        sourceObject.setX("7");
        sourceObject.setY(null);  // is null

        Destination destinationObject = new Destination();
        destinationObject.setA("aorig");
        destinationObject.setB("borig");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .bind(source::getY, destination::setB,
                                BindingOption.withNullSubstitution("XYZ"),  // null substitution enabled
                                BindingOption.mapWhen(() -> source.getX().length() == 1)) // is true
                ).buildMapper();
        mapper.map(sourceObject, destinationObject);

        // THEN
        assertEquals("Invalid 'a' property value.", "7", destinationObject.getA());
        assertEquals("Invalid 'b' property value.", "XYZ", destinationObject.getB());
    }
}
