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

public class CastOrMapIfPossibleOptionTest {

    public static class InnerSource {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class InnerDestination {

        private String valueDuplicated;

        private int flag;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getValueDuplicated() {
            return valueDuplicated;
        }

        public void setValueDuplicated(String valueDuplicated) {
            this.valueDuplicated = valueDuplicated;
        }
    }

    public static class Source {

        private InnerSource inner;

        public InnerSource getInner() {
            return inner;
        }

        public void setInner(InnerSource inner) {
            this.inner = inner;
        }
    }

    public static class Destination {

        private InnerDestination inner;

        public InnerDestination getInner() {
            return inner;
        }

        public void setInner(InnerDestination inner) {
            this.inner = inner;
        }
    }

    @Test
    public void when_inner_class_mapping_is_available_then_should_be_used_by_convention() {
        // GIVEN
        final String sampleValue = "abc";
        final String sampleValueDuplicated = sampleValue + sampleValue;

        InnerSource innerSource = new InnerSource();
        innerSource.setValue(sampleValue);

        Source sourceInstance = new Source();
        sourceInstance.setInner(innerSource);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(InnerSource.class, InnerDestination.class, (config, source, destination)
                        -> config.bind(() -> source.getValue() + source.getValue(), destination::setValueDuplicated))
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.useConvention(NameBasedMappingConvention.getStrictMatch().castOrMapIfPossible()))
                .buildMapper();

        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertNotNull("result.getInner() is null", result.getInner());
        assertEquals("Invalid result.getInner().getValueDuplicated()",
                sampleValueDuplicated,
                result.getInner().getValueDuplicated());
    }

    @Test
    public void convention_should_not_create_inner_class_when_istance_already_exists() {
        // GIVEN
        final int initialFlagValue = 2;

        InnerSource innerSource = new InnerSource();
        innerSource.setValue("abc");

        Source sourceInstance = new Source();
        sourceInstance.setInner(innerSource);

        InnerDestination innerDestination = new InnerDestination();
        innerDestination.setFlag(initialFlagValue);

        Destination destinationInstance = new Destination();
        destinationInstance.setInner(innerDestination);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(InnerSource.class, InnerDestination.class, (config, source, destination)
                        -> config.bind(source::getValue, destination::setValueDuplicated))
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.useConvention(NameBasedMappingConvention.getStrictMatch().castOrMapIfPossible()))
                .buildMapper();

        mapper.map(sourceInstance, destinationInstance);

        // THEN
        assertEquals("Why flag value has been changed?", initialFlagValue, destinationInstance.getInner().getFlag());
    }
}
