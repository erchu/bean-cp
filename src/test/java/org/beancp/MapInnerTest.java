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

public class MapInnerTest {

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

        private SourceSecondInner secondInner;

        public SourceSecondInner getSecondInner() {
            return secondInner;
        }

        public void setSecondInner(SourceSecondInner secondInner) {
            this.secondInner = secondInner;
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

        private int a, b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static class DestinationInner {

        private int a, b;

        private DestinationSecondInner secondDestinationInner;

        public DestinationSecondInner getSecondDestinationInner() {
            return secondDestinationInner;
        }

        public void setSecondDestinationInner(DestinationSecondInner secondDestinationInner) {
            this.secondDestinationInner = secondDestinationInner;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static class DestinationOuter {

        private DestinationInner innerDestinationProperty;

        public DestinationInner innerDestinationField;

        public DestinationInner getInnerDestinationProperty() {
            return innerDestinationProperty;
        }

        public void setInnerDestinationProperty(DestinationInner innerDestinationProperty) {
            this.innerDestinationProperty = innerDestinationProperty;
        }
    }

    @Test
    public void mapper_should_map_inner_classes() {
        // GIVEN
        SourceSecondInner sourceSecondInnerForField = new SourceSecondInner();
        sourceSecondInnerForField.setX(10);

        SourceInner sourceInnerForField = new SourceInner();
        sourceInnerForField.setSecondInner(sourceSecondInnerForField);
        sourceInnerForField.setX(20);

        SourceSecondInner sourceSecondInnerForProperty = new SourceSecondInner();
        sourceSecondInnerForProperty.setX(30);

        SourceInner sourceInnerForProperty = new SourceInner();
        sourceInnerForProperty.setSecondInner(sourceSecondInnerForProperty);
        sourceInnerForProperty.setX(40);

        SourceOuter sourceOuter = new SourceOuter();
        sourceOuter.innerField = sourceInnerForField;
        sourceOuter.setInnerProperty(sourceInnerForProperty);

        Mapper mapper = new MapperBuilder()
                .addMap(SourceSecondInner.class, DestinationSecondInner.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .addMap(SourceInner.class, DestinationInner.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .mapInner(
                                source::getSecondInner,
                                destination::setSecondDestinationInner,
                                destination::getSecondDestinationInner,
                                DestinationSecondInner.class))
                .addMap(SourceOuter.class, DestinationOuter.class,
                        (config, source, destination) -> config
                        .mapInner(
                                source::getInnerProperty,
                                destination::setInnerDestinationProperty,
                                destination::getInnerDestinationProperty,
                                DestinationInner.class)
                        .mapInner(() -> source.innerField, (DestinationInner v) -> {
                            destination.innerDestinationField = v;
                        }, DestinationInner.class))
                .buildMapper();

        // WHEN
        DestinationOuter destinationOuter = new DestinationOuter();
        mapper.map(sourceOuter, destinationOuter);

        // THEN
        assertEquals(
                "Invalid value for destinationOuter.getInnerDestinationProperty().getA()",
                sourceOuter.getInnerProperty().getX(),
                destinationOuter.getInnerDestinationProperty().getA());

        assertEquals(
                "Invalid value for destinationOuter.getInnerDestinationProperty().getSecondDestinationInner().getA()",
                sourceOuter.getInnerProperty().getSecondInner().getX(),
                destinationOuter.getInnerDestinationProperty().getSecondDestinationInner().getA());

        assertEquals(
                "Invalid value for destinationOuter.innerDestinationField.getA()",
                sourceOuter.innerField.getX(),
                destinationOuter.innerDestinationField.getA());

        assertEquals(
                "Invalid value for destinationOuter.innerDestinationField.getSecondDestinationInner().getA()",
                sourceOuter.innerField.getSecondInner().getX(),
                destinationOuter.innerDestinationField.getSecondDestinationInner().getA());
    }

    @Test
    public void mapper_construct_inner_classes_during_mapping_only_if_they_dont_exist_already() {
        // GIVEN
        SourceSecondInner sourceSecondInnerForField = new SourceSecondInner();
        sourceSecondInnerForField.setX(10);

        SourceInner sourceInnerForField = new SourceInner();
        sourceInnerForField.setSecondInner(sourceSecondInnerForField);
        sourceInnerForField.setX(20);

        SourceSecondInner sourceSecondInnerForProperty = new SourceSecondInner();
        sourceSecondInnerForProperty.setX(30);

        SourceInner sourceInnerForProperty = new SourceInner();
        sourceInnerForProperty.setSecondInner(sourceSecondInnerForProperty);
        sourceInnerForProperty.setX(40);

        SourceOuter sourceOuter = new SourceOuter();
        sourceOuter.innerField = sourceInnerForField;
        sourceOuter.setInnerProperty(sourceInnerForProperty);

        final int sampleInt1 = 50, sampleInt2 = 60;

        DestinationSecondInner destinationSecondInner = new DestinationSecondInner();
        destinationSecondInner.setB(sampleInt1);

        DestinationInner destinationInner = new DestinationInner();
        destinationInner.setSecondDestinationInner(destinationSecondInner);
        destinationInner.setB(sampleInt2);

        DestinationOuter destinationOuter = new DestinationOuter();
        destinationOuter.setInnerDestinationProperty(destinationInner);

        Mapper mapper = new MapperBuilder()
                .addMap(SourceSecondInner.class, DestinationSecondInner.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA))
                .addMap(SourceInner.class, DestinationInner.class,
                        (config, source, destination) -> config
                        .bind(source::getX, destination::setA)
                        .mapInner(
                                source::getSecondInner,
                                destination::setSecondDestinationInner,
                                destination::getSecondDestinationInner,
                                DestinationSecondInner.class))
                .addMap(SourceOuter.class, DestinationOuter.class,
                        (config, source, destination) -> config
                        .mapInner(
                                source::getInnerProperty,
                                destination::setInnerDestinationProperty,
                                destination::getInnerDestinationProperty,
                                DestinationInner.class)
                        .mapInner(() -> source.innerField, (DestinationInner v) -> {
                            destination.innerDestinationField = v;
                        }, DestinationInner.class))
                .buildMapper();

        // WHEN
        mapper.map(sourceOuter, destinationOuter);

        // THEN
        assertEquals(
                "Invalid value for destinationOuter.getInnerDestinationProperty().getB()",
                sampleInt2,
                destinationOuter.getInnerDestinationProperty().getB());

        assertEquals(
                "Invalid value for destinationOuter.getInnerDestinationProperty().getSecondDestinationInner().getB()",
                sampleInt1,
                destinationOuter.getInnerDestinationProperty().getSecondDestinationInner().getB());
    }
}
