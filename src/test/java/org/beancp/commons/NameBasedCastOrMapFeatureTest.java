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

import java.util.Collection;
import java.util.LinkedList;
import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class NameBasedCastOrMapFeatureTest {

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

    public static class SourceForCastingTest {

        private LinkedList<String> canBeCastedInherited;

        private Collection<String> haveToBeSkipped;

        public LinkedList<String> getCanBeCastedInherited() {
            return canBeCastedInherited;
        }

        public void setCanBeCastedInherited(LinkedList<String> canBeCastedInherited) {
            this.canBeCastedInherited = canBeCastedInherited;
        }

        public Collection<String> getHaveToBeSkipped() {
            return haveToBeSkipped;
        }

        public void setHaveToBeSkipped(Collection<String> haveToBeSkipped) {
            this.haveToBeSkipped = haveToBeSkipped;
        }
    }

    public static class DestinationForCastingTest {

        private Collection<String> canBeCastedInherited;

        private LinkedList<String> haveToBeSkipped;

        public Collection<String> getCanBeCastedInherited() {
            return canBeCastedInherited;
        }

        public void setCanBeCastedInherited(Collection<String> canBeCastedInherited) {
            this.canBeCastedInherited = canBeCastedInherited;
        }

        public LinkedList<String> getHaveToBeSkipped() {
            return haveToBeSkipped;
        }

        public void setHaveToBeSkipped(LinkedList<String> haveToBeSkipped) {
            this.haveToBeSkipped = haveToBeSkipped;
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

    @Test
    public void should_cast_to_base_class_wherever_it_is_possible() {
        // GIVEN
        SourceForCastingTest sourceInstance = new SourceForCastingTest();
        sourceInstance.setCanBeCastedInherited(new LinkedList<>());
        sourceInstance.setHaveToBeSkipped(new LinkedList<>());

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceForCastingTest.class, DestinationForCastingTest.class, (config, source, destination) ->
                        config.useConvention(NameBasedMappingConvention.getStrictMatch().castOrMapIfPossible())
                )
                .buildMapper();
        
        DestinationForCastingTest result = mapper.map(sourceInstance, DestinationForCastingTest.class);
        
        // THEN
        assertTrue("Invalid 'canBeCastedInherited' value.", sourceInstance.getCanBeCastedInherited() == result.getCanBeCastedInherited());
        assertNull("Invalid 'haveToBeSkipped' value.", result.getHaveToBeSkipped());
    }
}
