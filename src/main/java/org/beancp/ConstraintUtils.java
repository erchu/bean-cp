///*
// * bean-cp
// * Copyright (c) 2014, Rafal Chojnacki, All rights reserved.
// * 
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 3.0 of the License, or (at your option) any later version.
// * 
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library.
// */
//package org.beancp;
//
///**
// * Validations for common constraints.
// */
//public class ConstraintUtils {
//
//    private ConstraintUtils() {
//        throw new IllegalStateException("Not allowed to create instance of this class");
//    }
//
//    /**
//     * Will throw {@link NullParameterException} if {@code parameterValue} is null.
//     *
//     * @param parameterValue parameter value to check.
//     * @param parameterName parameter name.
//     */
//    public static void notNull(Object parameterValue, String parameterName) {
//        if (parameterValue == null) {
//            throw new NullParameterException(parameterName);
//        }
//    }
//
//    /**
//     * Will throw {@link IllegalArgumentException} if {@code constraint} is equal to true.
//     *
//     * @param constraint value to check.
//     * @param message exception message in format used by String.format.
//     * @param args arguments referenced by the format specifiers in the format string.
//     */
//    public static void failIfTrue(boolean constraint, final String message, final Object... args) {
//        if (constraint) {
//            throw new IllegalArgumentException(String.format(message, args));
//        }
//    }
//
//    /**
//     * Will throw {@link IllegalArgumentException} if {@code constraint} is equal to false.
//     *
//     * @param constraint value to check.
//     * @param message exception message in format used by String.format.
//     * @param args arguments referenced by the format specifiers in the format string.
//     */
//    public static void failIfFalse(boolean constraint, final String message, final Object... args) {
//        if (constraint == false) {
//            throw new IllegalArgumentException(String.format(message, args));
//        }
//    }
//}
