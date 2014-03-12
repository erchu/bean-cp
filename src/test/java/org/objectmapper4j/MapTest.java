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
import org.junit.Ignore;

public class MapTest {

    @Test
    public void mapper_should_work_when_mapped_properties_are_final()
            throws NoSuchFieldException {
        // GIVEN
        SourceWithFinalMembers sampleSource = new SourceWithFinalMembers();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithFinalMembers.class, DestinationWithFinalMembers.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        DestinationWithFinalMembers result = new DestinationWithFinalMembers();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
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

    @Test
    @Ignore("Subtype mappings not implemented for now.")
    public void mapper_should_work_with_inherited_classes_and_classes_with_protected_default_constructor()
            throws NoSuchFieldException {
        // GIVEN
        InheritedFromSourceWithWithProtectedDefaultConstructor sampleSource
                = new InheritedFromSourceWithWithProtectedDefaultConstructor();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceWithFinalMembers.class, DestinationWithProtectedDefaultConstructor.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        DestinationWithProtectedDefaultConstructor result
                = new InheritedFromDestinationWithProtectedDefaultConstructor();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
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

    public static class InheritedFromDestinationWithProtectedDefaultConstructor
            extends DestinationWithProtectedDefaultConstructor {
    }

    @Test
    public void mapper_should_work_with_final_classes()
            throws NoSuchFieldException {
        // GIVEN
        FinalSource sampleSource = new FinalSource();
        sampleSource.setX("xval");

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(FinalSource.class, FinalDestination.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .buildMapper();

        FinalDestination result = new FinalDestination();
        mapper.map(sampleSource, result);

        // THEN
        assertEquals("Property 'x' is not mapped correctly.", "xval", result.getA());
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

    public static class FinalDestination {

        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_objects_with_private_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(PrivateDefaultConstructorSource.class, PrivateDefaultConstructorDestination.class,
                        (config, source, destination) -> {});
    }

    public static class PrivateDefaultConstructorSource {
        
        private PrivateDefaultConstructorSource() {}
    }

    public static class PrivateDefaultConstructorDestination {
        
        private PrivateDefaultConstructorDestination() {}
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_objects_with_no_default_constructor()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(NoDefaultConstructorSource.class, NoDefaultConstructorDestination.class,
                        (config, source, destination) -> {});
    }

    public static class NoDefaultConstructorSource {
        
        public NoDefaultConstructorSource(final int x) {
        }
    }

    public static class NoDefaultConstructorDestination {
        
        public NoDefaultConstructorDestination(final int y) {
        }
    }

    @Test(expected = MapConfigurationException.class)
    public void mapper_should_not_accept_objects_of_non_static_classes()
            throws NoSuchFieldException {
        new MapperBuilder()
                .addMap(NonStaticSource.class, NonStaticDestination.class,
                        (config, source, destination) -> {});
    }

    public class NonStaticSource {
    }

    public class NonStaticDestination {
    }
}
