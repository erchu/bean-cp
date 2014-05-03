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
import org.junit.Test;
import static org.junit.Assert.*;


public class MappingObjectGraphByConventionTest {

    public static class SourceSecondInner {

        private int x;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }

    public static class SourceInner {

        private int x, y;

        private SourceSecondInner inner;

        public SourceSecondInner getInner() {
            return inner;
        }

        public void setInner(SourceSecondInner inner) {
            this.inner = inner;
        }

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

    public static class SourceOuter {

        private SourceInner innerProperty;

        public SourceInner innerField;

        public SourceInner getInnerProperty() {
            return innerProperty;
        }

        public void setInnerProperty(SourceInner innerProperty) {
            this.innerProperty = innerProperty;
        }
    }

    public static class DestinationSecondInner {

        private int x;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }

    public static class DestinationInner {

        private int x, y;

        private DestinationSecondInner inner;

        public DestinationSecondInner getInner() {
            return inner;
        }

        public void setInner(DestinationSecondInner inner) {
            this.inner = inner;
        }

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

    public static class DestinationOuter {

        private DestinationInner innerProperty;

        public DestinationInner innerField;

        public DestinationInner getInnerProperty() {
            return innerProperty;
        }

        public void setInnerProperty(DestinationInner innerProperty) {
            this.innerProperty = innerProperty;
        }
    }

    @Test
    public void conventions_can_be_used_to_map_object_graphs() {
        // GIVEN
        SourceSecondInner sourceSecondInnerForField = new SourceSecondInner();
        sourceSecondInnerForField.setX(10);

        SourceInner sourceInnerForField = new SourceInner();
        sourceInnerForField.setInner(sourceSecondInnerForField);
        sourceInnerForField.setX(20);
        sourceInnerForField.setY(30);

        SourceSecondInner sourceSecondInnerForProperty = new SourceSecondInner();
        sourceSecondInnerForProperty.setX(40);

        SourceInner sourceInnerForProperty = new SourceInner();
        sourceInnerForProperty.setInner(sourceSecondInnerForProperty);
        sourceInnerForProperty.setX(50);
        sourceInnerForProperty.setY(60);

        SourceOuter sourceOuter = new SourceOuter();
        sourceOuter.innerField = sourceInnerForField;
        sourceOuter.setInnerProperty(sourceInnerForProperty);
        
        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMappingConvention.getStrictMatch().castOrMapIfPossible())
                .buildMapper();
        
        DestinationOuter result = mapper.map(sourceOuter, DestinationOuter.class);
        
        // THEN

        assertNotNull("Result should be not null.", result);
        assertNotNull("result.innerField should be not null.", result.innerField);
        assertNotNull("result.getInnerProperty() should be not null.", result.getInnerProperty());
        assertNotNull("result.innerField.getInner() should be not null.", result.innerField.getInner());
        assertNotNull("result.getInnerProperty().getInner() should be not null.", result.getInnerProperty().getInner());
        
        assertEquals("Invalid result.innerField.getX() value.", sourceOuter.innerField.getX(), result.innerField.getX());
        assertEquals("Invalid result.innerField.getY() value.", sourceOuter.innerField.getY(), result.innerField.getY());
        assertEquals("Invalid result.innerField.getInner().getX() value.", sourceOuter.innerField.getInner().getX(), result.innerField.getInner().getX());
        
        assertEquals("Invalid result.getInnerProperty().getX() value.", sourceOuter.getInnerProperty().getX(), result.getInnerProperty().getX());
        assertEquals("Invalid result.getInnerProperty().getY() value.", sourceOuter.getInnerProperty().getY(), result.getInnerProperty().getY());
        assertEquals("Invalid result.getInnerProperty().getInner().getX() value.", sourceOuter.getInnerProperty().getInner().getX(), result.getInnerProperty().getInner().getX());
    }
}
