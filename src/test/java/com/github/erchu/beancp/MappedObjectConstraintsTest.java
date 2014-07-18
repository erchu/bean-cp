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
package com.github.erchu.beancp;

import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.MapperConfigurationException;
import com.github.erchu.beancp.Mapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappedObjectConstraintsTest {

    public static class Source {

        private String a;

        private String b;

        private String x;

        private String y;

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

        private String x;

        private String y;

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

    @Test
    public void mapper_should_work_when_mapped_properties_are_final() throws NoSuchFieldException {
        // GIVEN
        SourceWithFinalMembers sampleSource = new SourceWithFinalMembers();
        sampleSource.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithFinalMembers.class, DestinationWithFinalMembers.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // WHEN
        DestinationWithFinalMembers result = new DestinationWithFinalMembers();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", sampleSource.getX(), result.getA());
    }

    public static class SourceWithFinalMembers {

        private String x;

        protected SourceWithFinalMembers() {
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }
    }

    public static class DestinationWithFinalMembers {

        private String a;

        public final String getA() {
            return a;
        }

        public final void setA(String a) {
            this.a = a;
        }
    }

    public static final class FinalSource {

        private String x;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public final static class FinalDestination {

        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public final static class FinalDestinationWithProtectedDefaultConstructor {

        private String a;

        protected FinalDestinationWithProtectedDefaultConstructor() {
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static class PrivateDefaultConstructorSource {

        private PrivateDefaultConstructorSource() {
        }
    }

    public static class PrivateDefaultConstructorDestination {

        private PrivateDefaultConstructorDestination() {
        }
    }

    public class NonStaticSource {
    }

    public class NonStaticDestination {
    }

    public static class NoDefaultConstructorSource {

        public NoDefaultConstructorSource(final int x) {
        }
    }

    public static class NoDefaultConstructorDestination {

        public NoDefaultConstructorDestination(final int y) {
        }
    }

    public static class SourceWithWithProtectedDefaultConstructor {

        private String x;

        protected SourceWithWithProtectedDefaultConstructor() {
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public static final class SourceFinalWithWithProtectedDefaultConstructor {

        private String x;

        protected SourceFinalWithWithProtectedDefaultConstructor() {
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }
    }

    public static class InheritedFromSourceWithWithProtectedDefaultConstructor
            extends SourceWithWithProtectedDefaultConstructor {
    }

    public static class DestinationWithProtectedDefaultConstructor {

        private String a;

        protected DestinationWithProtectedDefaultConstructor() {
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static final class DestinationFinalWithProtectedDefaultConstructor {

        private String a;

        protected DestinationFinalWithProtectedDefaultConstructor() {
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static class InheritedFromDestinationWithProtectedDefaultConstructor
            extends DestinationWithProtectedDefaultConstructor {
    }

    @Test
    public void mapper_should_accept_final_source_classes()
            throws NoSuchFieldException {
        // GIVEN
        FinalSource sampleSource = new FinalSource();
        sampleSource.setX("xval");

        Mapper mapper = new MapperBuilder()
                .addMap(FinalSource.class, Destination.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // WHEN
        Destination result = new Destination();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
    }

    @Test
    public void mapper_should_accept_classes_with_protected_default_constructor_when_class_is_not_final()
            throws NoSuchFieldException {
        // GIVEN
        Source sampleSource = new Source();
        sampleSource.setX("xval");

        // WHEN
        new MapperBuilder()
                .addMap(SourceWithWithProtectedDefaultConstructor.class, DestinationWithProtectedDefaultConstructor.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: No error
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_source_classes_with_protected_default_constructor_when_class_is_final()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(SourceFinalWithWithProtectedDefaultConstructor.class, Destination.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: expect exception
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_destination_classes_with_protected_default_constructor_when_class_is_final_and_constructDestinationObjectUsing_is_not_available()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, DestinationFinalWithProtectedDefaultConstructor.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: expect exception
    }

    @Test
    public void mapper_should_accept_final_destination_classes_when_default_constructor_is_public()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, FinalDestination.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_accept_final_destination_classes_with_protected_default_constructor()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, FinalDestinationWithProtectedDefaultConstructor.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_source_classes_with_private_default_constructor()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(PrivateDefaultConstructorSource.class, Destination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_destination_classes_with_private_default_constructor()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, PrivateDefaultConstructorDestination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_non_static_source_classes()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(NonStaticSource.class, Destination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_non_static_destination_classes()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, NonStaticDestination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_source_classes_with_no_default_constructor()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(NoDefaultConstructorSource.class, Destination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }

    @Test(expected = MapperConfigurationException.class)
    public void mapper_should_not_accept_destination_classes_with_no_default_constructor_when_constructDestinationObjectUsing_is_not_privided()
            throws NoSuchFieldException {
        // WHEN
        new MapperBuilder()
                .addMap(Source.class, NoDefaultConstructorDestination.class,
                        (config, source, destination) -> {
                        });

        // THEN: exception expected
    }
}
