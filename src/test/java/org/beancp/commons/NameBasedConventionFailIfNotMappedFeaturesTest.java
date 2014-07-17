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
package org.beancp.commons;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.beancp.MapperConfigurationException;
import org.junit.Test;

public class NameBasedConventionFailIfNotMappedFeaturesTest {

    public static class MinimalSource {

        private int x;

        public int y;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static class ExtendedSource extends MinimalSource {

        private int z;

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

    public static class ExtendedDestination extends MinimalDestination {

        private int x;

        public int y;

        protected int nonPublicField;

        private int nonPublicProperty;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        protected int getNonPublicProperty() {
            return nonPublicProperty;
        }

        protected void setNonPublicProperty(int nonPublicProperty) {
            this.nonPublicProperty = nonPublicProperty;
        }
    }

    public static class MinimalDestination {

        private int z;

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

    @Test
    public void when_all_properties_are_mapped_then_should_be_no_error() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(ExtendedSource.class, ExtendedDestination.class,
                        (config, source, destination)
                        -> config.useConvention(
                                NameBasedMapConvention.get()
                                .failIfNotAllDestinationMembersMapped()
                                .failIfNotAllSourceMembersMapped()
                        )).buildMapper();

        // WHEN
        mapper.map(new ExtendedSource(), ExtendedDestination.class);
        
        // THEN: expect no error
    }

    @Test(expected = MapperConfigurationException.class)
    public void when_failIfNotAllDestinationMembersMapped_options_is_used_and_no_all_destination_members_are_mapped_then_exception_should_be_thrown() {
        // WHEN
        new MapperBuilder()
                .addMap(MinimalSource.class, ExtendedDestination.class,
                        (config, source, destination)
                        -> config.useConvention(
                                NameBasedMapConvention.get()
                                .failIfNotAllDestinationMembersMapped()
                        )).buildMapper();

        // THEN: expect exception
    }

    @Test
    public void when_failIfNotAllDestinationMembersMapped_options_is_NOT_used_and_no_all_destination_members_are_mapped_then_should_be_no_error() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(MinimalSource.class, ExtendedDestination.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())).buildMapper();


        // WHEN
        mapper.map(new MinimalSource(), ExtendedDestination.class);

        // THEN: expect no error
    }

    @Test
    public void failIfNotAllDestinationMembersMapped_should_not_throw_exception_for_explicity_excluded_destination_members() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(MinimalSource.class, ExtendedDestination.class,
                        (config, source, destination)
                        -> config.useConvention(
                                NameBasedMapConvention.get()
                                .failIfNotAllDestinationMembersMapped()
                                .excludeDestinationMembers("z")
                        )).buildMapper();


        // WHEN
        mapper.map(new MinimalSource(), ExtendedDestination.class);

        // THEN: expect no error
    }

    @Test(expected = MapperConfigurationException.class)
    public void when_failIfNotAllSourceMembersMapped_options_is_used_and_no_all_source_members_are_mapped_then_exception_should_be_thrown() {
        // WHEN
        new MapperBuilder()
                .addMap(ExtendedSource.class, MinimalDestination.class,
                        (config, source, destination)
                        -> config.useConvention(
                                NameBasedMapConvention.get()
                                .failIfNotAllSourceMembersMapped()
                        )).buildMapper();

        // THEN: expect exception
    }

    @Test
    public void when_failIfNotAllSourceMembersMapped_options_is_NOT_used_and_no_all_destination_members_are_mapped_then_should_be_no_error() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(ExtendedSource.class, MinimalDestination.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get())).buildMapper();

        // WHEN
        mapper.map(new ExtendedSource(), MinimalDestination.class);

        // THEN: expect no error
    }
}
