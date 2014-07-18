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
package com.github.erchu.beancp.commons;

import com.github.erchu.beancp.commons.NumberConverters;
import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

//WARNING: TEST METHODS CODE IS GENERATED TO CHECK ALL POSSIBLE CONVERSIONS BETWEEN:
//
//byte
//short
//int
//long
//float
//double
//java.lang.Byte
//java.lang.Short
//java.lang.Integer
//java.lang.Long
//java.lang.Float
//java.lang.Double
public class NumberConvertersTest {

    public static class Source {

        private byte byteValue;

        private short shortValue;

        private int intValue;

        private long longValue;

        private float floatValue;

        private double doubleValue;

        private Byte byteWrapperValue;

        private Short shortWrapperValue;

        private Integer intWrapperValue;

        private Long longWrapperValue;

        private Float floatWrapperValue;

        private Double doubleWrapperValue;

        public byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(byte byteValue) {
            this.byteValue = byteValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public Byte getByteWrapperValue() {
            return byteWrapperValue;
        }

        public void setByteWrapperValue(Byte byteWrapperValue) {
            this.byteWrapperValue = byteWrapperValue;
        }

        public Short getShortWrapperValue() {
            return shortWrapperValue;
        }

        public void setShortWrapperValue(Short shortWrapperValue) {
            this.shortWrapperValue = shortWrapperValue;
        }

        public Integer getIntWrapperValue() {
            return intWrapperValue;
        }

        public void setIntWrapperValue(Integer intWrapperValue) {
            this.intWrapperValue = intWrapperValue;
        }

        public Long getLongWrapperValue() {
            return longWrapperValue;
        }

        public void setLongWrapperValue(Long longWrapperValue) {
            this.longWrapperValue = longWrapperValue;
        }

        public Float getFloatWrapperValue() {
            return floatWrapperValue;
        }

        public void setFloatWrapperValue(Float floatWrapperValue) {
            this.floatWrapperValue = floatWrapperValue;
        }

        public Double getDoubleWrapperValue() {
            return doubleWrapperValue;
        }

        public void setDoubleWrapperValue(Double doubleWrapperValue) {
            this.doubleWrapperValue = doubleWrapperValue;
        }
    }

    public static class Destination {

        private byte byteValue;

        private short shortValue;

        private int intValue;

        private long longValue;

        private float floatValue;

        private double doubleValue;

        private Byte byteWrapperValue;

        private Short shortWrapperValue;

        private Integer intWrapperValue;

        private Long longWrapperValue;

        private Float floatWrapperValue;

        private Double doubleWrapperValue;

        public byte getByteValue() {
            return byteValue;
        }

        public void setByteValue(byte byteValue) {
            this.byteValue = byteValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }

        public Byte getByteWrapperValue() {
            return byteWrapperValue;
        }

        public void setByteWrapperValue(Byte byteWrapperValue) {
            this.byteWrapperValue = byteWrapperValue;
        }

        public Short getShortWrapperValue() {
            return shortWrapperValue;
        }

        public void setShortWrapperValue(Short shortWrapperValue) {
            this.shortWrapperValue = shortWrapperValue;
        }

        public Integer getIntWrapperValue() {
            return intWrapperValue;
        }

        public void setIntWrapperValue(Integer intWrapperValue) {
            this.intWrapperValue = intWrapperValue;
        }

        public Long getLongWrapperValue() {
            return longWrapperValue;
        }

        public void setLongWrapperValue(Long longWrapperValue) {
            this.longWrapperValue = longWrapperValue;
        }

        public Float getFloatWrapperValue() {
            return floatWrapperValue;
        }

        public void setFloatWrapperValue(Float floatWrapperValue) {
            this.floatWrapperValue = floatWrapperValue;
        }

        public Double getDoubleWrapperValue() {
            return doubleWrapperValue;
        }

        public void setDoubleWrapperValue(Double doubleWrapperValue) {
            this.doubleWrapperValue = doubleWrapperValue;
        }
    }

    @Test
    public void should_map_byte_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_byte_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_byte_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_byte_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_byte_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_byte_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_byte_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_byte_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_byte_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_byte_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_byte_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_byte_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_short_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_short_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_short_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_short_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_short_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_short_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_short_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_short_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_short_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_short_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_short_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_short_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_int_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_int_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_int_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_int_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_int_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_int_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_int_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_int_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_int_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_int_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_int_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_int_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntValue((int) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_long_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_long_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_long_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_long_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_long_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_long_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_long_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_long_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_long_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_long_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_long_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_long_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_float_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_float_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_float_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_float_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_float_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_float_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_float_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_float_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_float_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_float_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_float_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_float_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_double_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_double_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_double_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_double_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_double_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_double_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_double_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_double_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_double_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_double_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_double_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_double_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Byte_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Byte_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Byte_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Byte_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Byte_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Byte_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Byte_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Byte_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Byte_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Byte_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Byte_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Byte_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setByteWrapperValue((byte) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getByteWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Short_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Short_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Short_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Short_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Short_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Short_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Short_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Short_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Short_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Short_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Short_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Short_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setShortWrapperValue((short) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getShortWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Integer_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Integer_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Integer_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Integer_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Integer_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Integer_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Integer_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Integer_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Integer_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Integer_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Integer_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Integer_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setIntWrapperValue(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getIntWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Long_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Long_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Long_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Long_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Long_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Long_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Long_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Long_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Long_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Long_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Long_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Long_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setLongWrapperValue((long) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getLongWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Float_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Float_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Float_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Float_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Float_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Float_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Float_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Float_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Float_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Float_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Float_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Float_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setFloatWrapperValue((float) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getFloatWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Double_to_byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setByteValue, byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, result.getByteValue());
    }

    @Test
    public void should_map_Double_to_short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setShortValue, short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, result.getShortValue());
    }

    @Test
    public void should_map_Double_to_int() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setIntValue, int.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((int) 8, result.getIntValue());
    }

    @Test
    public void should_map_Double_to_long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setLongValue, long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, result.getLongValue());
    }

    @Test
    public void should_map_Double_to_float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setFloatValue, float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatValue(), 0.0);
    }

    @Test
    public void should_map_Double_to_double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setDoubleValue, double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleValue(), 0.0);
    }

    @Test
    public void should_map_Double_to_Byte() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setByteWrapperValue, Byte.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((byte) 8, (byte) result.getByteWrapperValue());
    }

    @Test
    public void should_map_Double_to_Short() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setShortWrapperValue, Short.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((short) 8, (short) result.getShortWrapperValue());
    }

    @Test
    public void should_map_Double_to_Integer() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setIntWrapperValue, Integer.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals(8, (int) result.getIntWrapperValue());
    }

    @Test
    public void should_map_Double_to_Long() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setLongWrapperValue, Long.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((long) 8, (long) result.getLongWrapperValue());
    }

    @Test
    public void should_map_Double_to_Float() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setFloatWrapperValue, Float.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((float) 8, result.getFloatWrapperValue(), 0.0);
    }

    @Test
    public void should_map_Double_to_Double() {
        // GIVEN
        Source sourceInstance = new Source();
        sourceInstance.setDoubleWrapperValue((double) 8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(NumberConverters.get())
                .addMap(Source.class, Destination.class, (config, source, destination)
                        -> config.mapInner(source::getDoubleWrapperValue, destination::setDoubleWrapperValue, Double.class))
                .buildMapper();
        Destination result = mapper.map(sourceInstance, Destination.class);

        // THEN
        assertEquals((double) 8, result.getDoubleWrapperValue(), 0.0);
    }

}
