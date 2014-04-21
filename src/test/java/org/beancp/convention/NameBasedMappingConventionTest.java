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
package org.beancp.convention;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class NameBasedMappingConventionTest {

    private static class XYSource {

        private int x, y;

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

    private static class XYDestination {

        private int x, y;

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

    @Test
    @Ignore(value = "")
    public void when_source_and_destination_classes_has_members_of_the_same_name_and_type_then_should_be_mapped() {
        // GIVEN
        XYSource sourceInstance = new XYSource();
        sourceInstance.setX(8);
        sourceInstance.setY(9);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(XYSource.class, XYDestination.class, (config, source, destination)
                        -> config.useConvention(NameBasedMappingConvention.getFlexibleMatch())
                ).buildMapper();
        
        XYDestination result = mapper.map(sourceInstance, XYDestination.class);

        // THEN
        assertEquals("Invalid 'x' property value.", sourceInstance.getX(), result.getX());
        assertEquals("Invalid 'y' property value.", sourceInstance.getY(), result.getY());
    }
}
