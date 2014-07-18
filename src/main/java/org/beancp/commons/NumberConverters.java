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

import java.util.function.Function;
import org.beancp.Converter;

/**
 * Conversions between primitive number types and its wrappers:
 * 
 * <ul>
 * <li>byte</li>
 * <li>short</li>
 * <li>int</li>
 * <li>long</li>
 * <li>float</li>
 * <li>double</li>
 * <li>java.lang.Byte</li>
 * <li>java.lang.Short</li>
 * <li>java.lang.Integer</li>
 * <li>java.lang.Long</li>
 * <li>java.lang.Float</li>
 * <li>java.lang.Double</li>
 * </ul>
 */
public class NumberConverters {

    private NumberConverters() {
        throw new AssertionError(
                String.format(
                        "Creating an instance of the %s class is not allowed.",
                        AssertionError.class));
    }

    /**
     * Returns number converters.
     * 
     * @return number converters.
     */
    public static Converter[] get() {
        return new Converter[]{
            new Converter(byte.class, Byte.class, ((Function<Byte, Byte>) (source -> source))),
            new Converter(short.class, Short.class, ((Function<Short, Short>) (source -> source))),
            new Converter(int.class, Integer.class, ((Function<Integer, Integer>) (source -> source))),
            new Converter(long.class, Long.class, ((Function<Long, Long>) (source -> source))),
            new Converter(float.class, Float.class, ((Function<Float, Float>) (source -> source))),
            new Converter(double.class, Double.class, ((Function<Double, Double>) (source -> source))),
            new Converter(Byte.class, Short.class, ((Function<Byte, Short>) (source -> source.shortValue()))),
            new Converter(Byte.class, Integer.class, ((Function<Byte, Integer>) (source -> source.intValue()))),
            new Converter(Byte.class, Long.class, ((Function<Byte, Long>) (source -> source.longValue()))),
            new Converter(Byte.class, Float.class, ((Function<Byte, Float>) (source -> source.floatValue()))),
            new Converter(Byte.class, Double.class, ((Function<Byte, Double>) (source -> source.doubleValue()))),
            new Converter(Short.class, Byte.class, ((Function<Short, Byte>) (source -> source.byteValue()))),
            new Converter(Short.class, Integer.class, ((Function<Short, Integer>) (source -> source.intValue()))),
            new Converter(Short.class, Long.class, ((Function<Short, Long>) (source -> source.longValue()))),
            new Converter(Short.class, Float.class, ((Function<Short, Float>) (source -> source.floatValue()))),
            new Converter(Short.class, Double.class, ((Function<Short, Double>) (source -> source.doubleValue()))),
            new Converter(Integer.class, Byte.class, ((Function<Integer, Byte>) (source -> source.byteValue()))),
            new Converter(Integer.class, Short.class, ((Function<Integer, Short>) (source -> source.shortValue()))),
            new Converter(Integer.class, Long.class, ((Function<Integer, Long>) (source -> source.longValue()))),
            new Converter(Integer.class, Float.class, ((Function<Integer, Float>) (source -> source.floatValue()))),
            new Converter(Integer.class, Double.class, ((Function<Integer, Double>) (source -> source.doubleValue()))),
            new Converter(Long.class, Byte.class, ((Function<Long, Byte>) (source -> source.byteValue()))),
            new Converter(Long.class, Short.class, ((Function<Long, Short>) (source -> source.shortValue()))),
            new Converter(Long.class, Integer.class, ((Function<Long, Integer>) (source -> source.intValue()))),
            new Converter(Long.class, Float.class, ((Function<Long, Float>) (source -> source.floatValue()))),
            new Converter(Long.class, Double.class, ((Function<Long, Double>) (source -> source.doubleValue()))),
            new Converter(Float.class, Byte.class, ((Function<Float, Byte>) (source -> source.byteValue()))),
            new Converter(Float.class, Short.class, ((Function<Float, Short>) (source -> source.shortValue()))),
            new Converter(Float.class, Integer.class, ((Function<Float, Integer>) (source -> source.intValue()))),
            new Converter(Float.class, Long.class, ((Function<Float, Long>) (source -> source.longValue()))),
            new Converter(Float.class, Double.class, ((Function<Float, Double>) (source -> source.doubleValue()))),
            new Converter(Double.class, Byte.class, ((Function<Double, Byte>) (source -> source.byteValue()))),
            new Converter(Double.class, Short.class, ((Function<Double, Short>) (source -> source.shortValue()))),
            new Converter(Double.class, Integer.class, ((Function<Double, Integer>) (source -> source.intValue()))),
            new Converter(Double.class, Long.class, ((Function<Double, Long>) (source -> source.longValue()))),
            new Converter(Double.class, Float.class, ((Function<Double, Float>) (source -> source.floatValue())))
        };
    }
}
