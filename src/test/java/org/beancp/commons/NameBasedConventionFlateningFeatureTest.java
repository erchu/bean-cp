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

import java.util.Random;
import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.beancp.MappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NameBasedConventionFlateningFeatureTest {

    public static class SourceLevel4 {

        private String name;

        public String model;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SourceLevel3 {

        private SourceLevel4 first;

        private SourceLevel4 second;

        public SourceLevel4 third;

        public SourceLevel4 getFirst() {
            return first;
        }

        public void setFirst(SourceLevel4 first) {
            this.first = first;
        }

        public SourceLevel4 getSecond() {
            return second;
        }

        public void setSecond(SourceLevel4 second) {
            this.second = second;
        }
    }

    public static class SourceLevel2 {

        private SourceLevel3 detail;

        private SourceLevel4 detailSecond;

        public SourceLevel3 getDetail() {
            return detail;
        }

        public void setDetail(SourceLevel3 detail) {
            this.detail = detail;
        }

        public SourceLevel4 getDetailSecond() {
            return detailSecond;
        }

        public void setDetailSecond(SourceLevel4 detailSecond) {
            this.detailSecond = detailSecond;
        }
    }

    public static class SourceTopLevel {

        private SourceLevel2 info;

        public SourceLevel2 getInfo() {
            return info;
        }

        public void setInfo(SourceLevel2 info) {
            this.info = info;
        }
    }

    public static class DestinationForSourceLevel3 {

        private String firstName;

        private String firstModel;

        private String thirdName;

        private String thirdModel;

        public String secondName;

        public String secondModel;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstModel() {
            return firstModel;
        }

        public void setFirstModel(String firstModel) {
            this.firstModel = firstModel;
        }

        public String getThirdName() {
            return thirdName;
        }

        public void setThirdName(String thirdName) {
            this.thirdName = thirdName;
        }

        public String getThirdModel() {
            return thirdModel;
        }

        public void setThirdModel(String thirdModel) {
            this.thirdModel = thirdModel;
        }
    }

    public static class DestinationForSourceLevel2 {

        private String detailSecondName;

        public String getDetailSecondName() {
            return detailSecondName;
        }

        public void setDetailSecondName(String detailSecondName) {
            this.detailSecondName = detailSecondName;
        }
    }

    public static class DestinationForSourceTopLevel {

        private String infoDetailFirstName;

        public String getInfoDetailFirstName() {
            return infoDetailFirstName;
        }

        public void setInfoDetailFirstName(String infoDetailFirstName) {
            this.infoDetailFirstName = infoDetailFirstName;
        }
    }

    private final Random random = new Random();

    private String generateRandomString() {
        int randomInt = random.nextInt();

        return "" + randomInt;
    }

    private SourceLevel4 getSampleSourceLevel4() {
        SourceLevel4 sourceLevel4instance1 = new SourceLevel4();
        sourceLevel4instance1.setName(generateRandomString());
        sourceLevel4instance1.model = generateRandomString();

        return sourceLevel4instance1;
    }

    private SourceLevel3 getSampleSourceLevel3() {
        SourceLevel4 sourceLevel4Instance1 = getSampleSourceLevel4();
        SourceLevel4 sourceLevel4Instance2 = getSampleSourceLevel4();
        SourceLevel4 sourceLevel4Instance3 = getSampleSourceLevel4();
        SourceLevel3 sourceLevel3 = new SourceLevel3();
        sourceLevel3.setFirst(sourceLevel4Instance1);
        sourceLevel3.setSecond(sourceLevel4Instance2);
        sourceLevel3.third = sourceLevel4Instance3;

        return sourceLevel3;
    }

    private SourceLevel2 getSampleSourceLevel2() {
        SourceLevel3 sourceLevel3 = getSampleSourceLevel3();
        SourceLevel4 sourceLevel4 = getSampleSourceLevel4();

        SourceLevel2 sourceLevel2 = new SourceLevel2();
        sourceLevel2.setDetail(sourceLevel3);
        sourceLevel2.setDetailSecond(sourceLevel4);

        return sourceLevel2;
    }

    private SourceTopLevel getSampleSourceTopLevel() {
        SourceLevel2 sourceLevel2 = getSampleSourceLevel2();

        SourceTopLevel sourceTopLevel = new SourceTopLevel();
        sourceTopLevel.setInfo(sourceLevel2);

        return sourceTopLevel;
    }

    @Test(expected = MappingException.class)
    public void when_flatening_is_not_enabled_then_convention_should_match_only_direct_members() {
        // GIVEN
        SourceLevel3 sourceInstance = getSampleSourceLevel3();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceLevel3.class, DestinationForSourceLevel3.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get()))
                .buildMapper();

        mapper.map(sourceInstance, DestinationForSourceLevel3.class);

        // THEN: expect exception, because no source member match destination member
    }

    @Test
    public void when_flatening_is_enabled_then_convention_should_look_for_matching_inner_members() {
        // GIVEN
        SourceLevel3 sourceInstance = getSampleSourceLevel3();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceLevel3.class, DestinationForSourceLevel3.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().enableFlattening()))
                .buildMapper();

        DestinationForSourceLevel3 result = mapper.map(sourceInstance, DestinationForSourceLevel3.class);

        // THEN
        assertEquals("Invalid result.getFirstModel() value", sourceInstance.getFirst().model, result.getFirstModel());
        assertEquals("Invalid result.getFirstName() value", sourceInstance.getFirst().getName(), result.getFirstName());
        assertEquals("Invalid result.secondModel value", sourceInstance.getSecond().model, result.secondModel);
        assertEquals("Invalid result.secondName value", sourceInstance.getSecond().getName(), result.secondName);
        assertEquals("Invalid result.getThirdModel() value", sourceInstance.third.model, result.getThirdModel());
        assertEquals("Invalid result.getThirdName() value", sourceInstance.third.getName(), result.getThirdName());
    }

    @Test
    public void when_flattening_then_null_at_intermediate_level_shoud_be_mapped_as_null() {
        // GIVEN
        SourceLevel3 sourceInstance = getSampleSourceLevel3();
        sourceInstance.setFirst(null);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceLevel3.class, DestinationForSourceLevel3.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().enableFlattening()))
                .buildMapper();

        DestinationForSourceLevel3 result = mapper.map(sourceInstance, DestinationForSourceLevel3.class);

        // THEN
        assertNull("Invalid result.getFirstModel() value", result.getFirstModel());
    }

    @Test
    public void flatening_feature_should_look_as_deep_as_possible() {
        // GIVEN
        SourceTopLevel sourceInstance = getSampleSourceTopLevel();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceTopLevel.class, DestinationForSourceTopLevel.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().enableFlattening()))
                .buildMapper();

        DestinationForSourceTopLevel result = mapper.map(sourceInstance, DestinationForSourceTopLevel.class);

        // THEN
        assertEquals(
                "Invalid result.getInfoDetailFirstName() value.",
                sourceInstance.getInfo().getDetail().getFirst().getName(),
                result.getInfoDetailFirstName());
    }

    @Test
    public void flatening_should_chose_as_shallowly_option_as_possible() {
        // GIVEN
        SourceLevel2 sourceInstance = getSampleSourceLevel2();

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SourceLevel2.class, DestinationForSourceLevel2.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().enableFlattening()))
                .buildMapper();

        DestinationForSourceLevel2 result = mapper.map(sourceInstance, DestinationForSourceLevel2.class);

        // THEN
        // getDetailSecond() has higher priority than getDetail().getSecond()
        assertEquals(
                "Invalid result.getDetailSecondName() value.",
                sourceInstance.getDetailSecond().getName(),
                result.getDetailSecondName());
    }
}
