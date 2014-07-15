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
package org.beancp.integration_tests;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CircularReferencesMappingTest {

    public static class ReferenceHolder<T> {

        private final T reference;

        public ReferenceHolder(final T reference) {
            this.reference = reference;
        }

        public T getReference() {
            return reference;
        }
    }

    public static class SourceHolder {

        private final Source reference;

        public SourceHolder(final Source reference) {
            this.reference = reference;
        }

        public Source getReference() {
            return reference;
        }
    }

    public static class Source {

        private final String value;

        private Source directReference;

        private ReferenceHolder<Source> indirectReferenceToMap;

        private SourceHolder indirectReferenceToConvert;

        public Source() {
            this.value = null;
        }

        public Source(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Source getDirectReference() {
            return directReference;
        }

        public void setDirectReference(final Source directReference) {
            this.directReference = directReference;
        }

        public ReferenceHolder<Source> getIndirectReferenceToMap() {
            return indirectReferenceToMap;
        }

        public void setIndirectReferenceToMap(final ReferenceHolder<Source> indirectReferenceToMap) {
            this.indirectReferenceToMap = indirectReferenceToMap;
        }

        public SourceHolder getIndirectReferenceToConvert() {
            return indirectReferenceToConvert;
        }

        public void setIndirectReferenceToConvert(SourceHolder indirectReferenceToConvert) {
            this.indirectReferenceToConvert = indirectReferenceToConvert;
        }
    }

    public static class Destination {

        private String value;

        private Destination directReference;

        private ReferenceHolder<Destination> indirectReferenceToMap;

        private Destination indirectReferenceToConvert;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Destination getDirectReference() {
            return directReference;
        }

        public void setDirectReference(final Destination directReference) {
            this.directReference = directReference;
        }

        public ReferenceHolder<Destination> getIndirectReferenceToMap() {
            return indirectReferenceToMap;
        }

        public void setIndirectReferenceToMap(final ReferenceHolder<Destination> indirectReferenceToMap) {
            this.indirectReferenceToMap = indirectReferenceToMap;
        }

        public Destination getIndirectReferenceToConvert() {
            return indirectReferenceToConvert;
        }

        public void setIndirectReferenceToConvert(Destination indirectReferenceToConvert) {
            this.indirectReferenceToConvert = indirectReferenceToConvert;
        }
    }

    @Test
    public void mapper_should_be_able_to_map_direct_and_indirect_circular_references() {
        // GIVEN
        Source sourceInstance = new Source("Hello!");
        sourceInstance.setDirectReference(sourceInstance);
        sourceInstance.setIndirectReferenceToMap(new ReferenceHolder<>(sourceInstance));

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(SourceHolder.class, Destination.class, (mapperRef, source)
                        -> mapperRef.map(source.getReference(), Destination.class))
                .addMap(Source.class, Destination.class, (config, source, destination) -> config
                        .bind(source::getValue, destination::setValue)
                        .mapInner(
                                source::getDirectReference,
                                destination::setDirectReference,
                                destination::getDirectReference,
                                Destination.class)
                        .mapInner(
                                () -> source.getIndirectReferenceToMap() != null ? source.getIndirectReferenceToMap().getReference() : null,
                                (Destination value) -> {
                                    destination.setIndirectReferenceToMap(new ReferenceHolder<>(value));
                                },
                                () -> destination.getIndirectReferenceToMap() != null ? destination.getIndirectReferenceToMap().getReference() : null,
                                Destination.class)
                        .mapInner(
                                source::getIndirectReferenceToConvert,
                                destination::setIndirectReferenceToConvert,
                                destination::getIndirectReferenceToConvert,
                                Destination.class))
                .buildMapper();

        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(
                "Invalid result.getValue()",
                sourceInstance.getValue(),
                result.getValue());

        assertEquals(
                "Invalid result.getDirectReference().getValue() value",
                sourceInstance.getDirectReference().getValue(),
                result.getDirectReference().getValue());

        assertEquals(
                "Invalid result.getIndirectReference().getReference().getValue() value",
                sourceInstance.getIndirectReferenceToMap().getReference().getValue(),
                result.getIndirectReferenceToMap().getReference().getValue());
    }
}
