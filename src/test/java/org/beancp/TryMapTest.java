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

import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

public class TryMapTest {

    public static class Source {
    }

    public static class Destination {
    }

    @Test
    public void tryMap_for_existing_objects_should_return_false_if_no_mapping_is_available() {
        // GIVEN
        Mapper mapper = new MapperBuilder().buildMapper();

        // WHEN
        boolean result = mapper.mapIfMapperAvailable(new Source(), new Destination());

        // THEN
        assertFalse("Invalid result.", result);
    }

    @Test
    public void tryMap_for_existing_objects_should_return_true_if_mapping_is_available() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> {
                })
                .buildMapper();

        // WHEN
        boolean result = mapper.mapIfMapperAvailable(new Source(), new Destination());

        // THEN
        assertTrue("Invalid result.", result);
    }

    @Test
    public void tryMap_for_mapping_to_new_destination_object_should_return_false_if_no_mapping_is_available() {
        // GIVEN
        Mapper mapper = new MapperBuilder().buildMapper();

        // WHEN
        Optional<Destination> result = mapper.mapIfMapperAvailable(new Source(), Destination.class);

        // THEN
        assertFalse("Invalid result.", result.isPresent());
    }

    @Test
    public void tryMap_for_mapping_to_new_destination_object_should_return_true_if_mapping_is_available() {
        // GIVEN
        Mapper mapper = new MapperBuilder()
                .addMap(Source.class, Destination.class, (config, source, destination) -> {
                })
                .buildMapper();

        // WHEN
        Optional<Destination> result = mapper.mapIfMapperAvailable(new Source(), Destination.class);

        // THEN
        assertTrue("Invalid result.", result.isPresent());
    }
}
