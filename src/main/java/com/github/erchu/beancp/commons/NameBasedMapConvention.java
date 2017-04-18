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

import com.github.erchu.beancp.FieldBindingSide;
import com.github.erchu.beancp.PropertyBindingSide;
import com.github.erchu.beancp.Binding;
import com.github.erchu.beancp.BindingWithValueConversion;
import com.github.erchu.beancp.BindingWithValueMap;
import com.github.erchu.beancp.BindingSide;
import com.github.erchu.beancp.MapConvention;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.github.erchu.beancp.MappingException;
import com.github.erchu.beancp.MappingInfo;
import static org.apache.commons.lang3.ObjectUtils.*;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.Validate.*;
import com.github.erchu.beancp.MapperConfigurationException;
import java.lang.reflect.Modifier;

/**
 * Convention matches fields by name.
 */
public class NameBasedMapConvention implements MapConvention {

    private enum MemberAccessType {

        FIELD,
        PROPERTY
    }

    private List<Predicate<String>> _includeDestinationMembers;

    private List<Predicate<String>> _excludeDestinationMembers;

    private boolean _flateningEnabled;

    private boolean _failIfNotAllDestinationMembersMapped;

    private boolean _failIfNotAllSourceMembersMapped;

    /**
     * Constructs new instance.
     */
    protected NameBasedMapConvention() {
    }

    /**
     * Returns mapping convention with the following configuration:
     *
     * <ul>
     * <li>No destination members excluded</li>
     * <li>Maximum possible number of destination members included</li>
     * <li>Will <b>not</b> fail if not all <b>destination</b> members are mapped</li>
     * <li>Will <b>not</b> fail if not all <b>source</b> members are mapped</li>
     * <li>Flattening feature <b>disabled</b></li>
     * </ul>
     *
     * @return mapping convention.
     */
    public static NameBasedMapConvention get() {
        NameBasedMapConvention defaultConvention = new NameBasedMapConvention();
        defaultConvention._excludeDestinationMembers = new LinkedList<>();
        defaultConvention._includeDestinationMembers = new LinkedList<>();
        defaultConvention._failIfNotAllDestinationMembersMapped = false;
        defaultConvention._failIfNotAllSourceMembersMapped = false;
        defaultConvention._flateningEnabled = false;

        return defaultConvention;
    }

    /**
     * Sets list of destination members which will be included by convention. Each entry must be
     * regular expression matching field name or bean property name (according to
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html">beans
     * specification</a>). If not specified (empty array) all members are subject to map by
     * convention. If specified (not empty array) only selected members could be mapped by
     * convention. This list has lower priority that exclude list specified by
     *
     * {@link #excludeDestinationMembers(java.lang.String...) } method. Note that when you put some
     * member on list then it is not guaranteed that it will be mapped &#8212; it still have to have
     * matching source's member according to convention configuration.
     *
     * @param members members to include
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention includeDestinationMembers(String... members) {
        notNull(members, "members");

        _includeDestinationMembers = toPredicates(members);

        return this;
    }

    /**
     * Sets list of destination members which will be excluded (ignored) by convention. Each entry
     * must be regular expression matching field name or bean property name (according to
     * <a href="http://www.oracle.com/technetwork/java/javase/documentation/spec-136004.html">beans
     * specification</a>). This list has higher priority that include list specified by
     * {@link #includeDestinationMembers(java.lang.String...)} method.
     *
     * @param members members to ignore
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention excludeDestinationMembers(String... members) {
        notNull(members, "members");

        _excludeDestinationMembers = toPredicates(members);

        return this;
    }

    /**
     * Enables flattening feature. This feature will try to match members from nested classes only
     * if no direct member can be matched. This is useful if you have complex model to be mapped to
     * simpler one. Destination member will be matched to source nested class member if destination
     * member's name match path to source member including nested classes (if flattening feature is
     * disabled then nested classes are ignored). For example: for below classes
     * {@code setCustomerName} will be matched to {@code getCustomer().getName()} because
     * {@code CustomerName} property can be interpreted as path to {@code Customer} and then to
     * {@code Name} property.
     *
     *
     * <pre>
     * class Customer {
     *
     *     private String name;
     *
     *     public String getName() {
     *          return name;
     *     }
     * }
     *
     * class Order {
     *
     *     private Customer customer;
     *
     *     public Customer getCustomer() {
     *          return customer;
     *     }
     * }
     *
     * class OrderDto {
     *
     *     private String customerName;
     *
     *     public void setCustomerName(final String customerName) {
     *          customerName = customerName;
     *     }
     *
     *     public String getCustomerName() {
     *          return customerName;
     *     }
     * }
     * </pre>
     *
     * <p>
     * This feature can be disabled by {@link #disableFlattening()} method.
     * </p>
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention enableFlattening() {
        _flateningEnabled = true;

        return this;
    }

    /**
     * Disables flattening feature as described in {@link #enableFlattening()} method. This is
     * opposite to {@link #enableFlattening()} method.
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention disableFlattening() {
        _flateningEnabled = false;

        return this;
    }

    /**
     * Convention will fail during map building (see
     * {@link #getBindings(com.github.erchu.beancp.MappingInfo, java.lang.Class, java.lang.Class)}
     * method) if not all destination members are mapped.
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention failIfNotAllDestinationMembersMapped() {
        _failIfNotAllDestinationMembersMapped = true;

        return this;
    }

    /**
     * Convention will fail during map building (see
     * {@link #getBindings(com.github.erchu.beancp.MappingInfo, java.lang.Class, java.lang.Class)}
     * method) if not all source members are mapped.
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention failIfNotAllSourceMembersMapped() {
        _failIfNotAllSourceMembersMapped = true;

        return this;
    }

    @Override
    public List<Binding> getBindings(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass) {
        List<Binding> result = new LinkedList<>();
        BeanInfo sourceBeanInfo, destinationBeanInfo;

        try {
            destinationBeanInfo = Introspector.getBeanInfo(destinationClass);
        } catch (IntrospectionException ex) {
            throw new MappingException(
                    String.format("Failed to get bean info for %s", destinationClass), ex);
        }

        try {
            sourceBeanInfo = Introspector.getBeanInfo(sourceClass);
        } catch (IntrospectionException ex) {
            throw new MappingException(
                    String.format("Failed to get bean info for %s", sourceClass), ex);
        }

        boolean allDestinationMembersMapped = true;

        for (PropertyDescriptor destinationProperty : destinationBeanInfo.getPropertyDescriptors()) {
            Method destinationMember = destinationProperty.getWriteMethod();

            if (destinationMember != null) {
                BindingSide destinationBindingSide
                        = new PropertyBindingSide(destinationProperty);

                if (isDestinationMemberExpectedToBind(destinationBindingSide) == false) {
                    continue;
                }

                List<BindingSide> sourceBindingSide
                        = getMatchingSourceMemberByName(sourceBeanInfo, sourceClass,
                                destinationProperty.getName(), MemberAccessType.PROPERTY);

                if (sourceBindingSide != null) {
                    BindingSide[] sourceBindingSideArray
                            = sourceBindingSide.stream().toArray(BindingSide[]::new);

                    Binding binding = getBindingIfAvailable(
                            sourceClass,
                            destinationClass,
                            mappingsInfo,
                            sourceBindingSideArray,
                            destinationBindingSide);

                    if (binding != null) {
                        result.add(binding);
                    }
                } else {
                    allDestinationMembersMapped = false;
                }
            }
        }

        for (Field destinationMember : getInstanceFields(destinationClass)) {
            if (Modifier.isFinal(destinationMember.getModifiers())) {
                continue;
            }
            
            BindingSide destinationBindingSide = new FieldBindingSide(destinationMember);

            if (isDestinationMemberExpectedToBind(destinationBindingSide) == false) {
                continue;
            }

            List<BindingSide> sourceBindingSide
                    = getMatchingSourceMemberByName(sourceBeanInfo, sourceClass,
                            destinationMember.getName(), MemberAccessType.FIELD);

            if (sourceBindingSide != null) {
                BindingSide[] sourceBindingSideArray
                        = sourceBindingSide.stream().toArray(BindingSide[]::new);

                Binding binding = getBindingIfAvailable(
                        sourceClass,
                        destinationClass,
                        mappingsInfo,
                        sourceBindingSideArray,
                        destinationBindingSide);

                if (binding != null) {
                    result.add(binding);
                }
            } else {
                allDestinationMembersMapped = false;
            }
        }

        if (_failIfNotAllDestinationMembersMapped) {
            if (allDestinationMembersMapped == false) {
                throw new MapperConfigurationException("Not all destination members are mapped."
                        + " This exception has been trown because "
                        + "failIfNotAllDestinationMembersMapped option is enabled.");
            }
        }

        if (_failIfNotAllSourceMembersMapped) {
            boolean allSourceMembersMapped = true;

            for (PropertyDescriptor sourceProperty : sourceBeanInfo.getPropertyDescriptors()) {
                Method sourceMember = sourceProperty.getReadMethod();

                if (sourceMember != null) {
                    if (sourceMember.getDeclaringClass().equals(Object.class)) {
                        continue;
                    }

                    BindingSide sourceBindingSide = new PropertyBindingSide(sourceProperty);

                    if (isSourceMemberMapped(result, sourceBindingSide) == false) {
                        allSourceMembersMapped = false;
                        break;
                    }
                }
            }

            // if all properties are mapped we still need to check fields
            if (allSourceMembersMapped) {
                for (Field sourceMember : getInstanceFields(sourceClass)) {
                    if (sourceMember.getDeclaringClass().equals(Object.class)) {
                        continue;
                    }

                    BindingSide sourceBindingSide = new FieldBindingSide(sourceMember);

                    if (isSourceMemberMapped(result, sourceBindingSide) == false) {
                        allSourceMembersMapped = false;
                        break;
                    }
                }
            }

            if (allSourceMembersMapped == false) {
                throw new MapperConfigurationException("Not all source members are mapped."
                        + " This exception has been trown because "
                        + "failIfNotAllSourceMembersMapped option is enabled.");
            }
        }

        return result;
    }

    private boolean isSourceMemberMapped(
            final List<Binding> allBindings, final BindingSide sourceBindingSide) {
        return allBindings.stream()
                .anyMatch(i -> i.getSourcePath()[0].equals(sourceBindingSide));
    }

    private boolean isDestinationMemberExpectedToBind(BindingSide destinationBindingSide) {
        if (anyPredicateMatch(_excludeDestinationMembers, destinationBindingSide)) {
            return false;
        }

        if (_includeDestinationMembers.isEmpty()) {
            return true;
        }

        return anyPredicateMatch(_includeDestinationMembers, destinationBindingSide);
    }

    private boolean anyPredicateMatch(
            final Collection<Predicate<String>> predicates,
            final BindingSide destinationBindingSide) {
        if (predicates.isEmpty()) {
            return false;
        }

        return predicates.stream()
                .anyMatch(i -> i.test(destinationBindingSide.getName()));
    }

    private List<BindingSide> getMatchingSourceMemberByName(
            final BeanInfo sourceBeanInfo,
            final Class sourceClass,
            final String atDestinationName,
            final MemberAccessType destinationMemberAccessType) {
        List<BindingSide> matchingSourcePropertyBindingSide = getMatchingPropertyByName(
                sourceBeanInfo, atDestinationName, destinationMemberAccessType);

        List<BindingSide> matchingSourceFieldBindingSide = getMatchingFieldByName(
                sourceClass, atDestinationName, destinationMemberAccessType);

        switch (destinationMemberAccessType) {
            case FIELD:
                return firstNonNull(
                        matchingSourceFieldBindingSide, matchingSourcePropertyBindingSide);
            case PROPERTY:
                return firstNonNull(
                        matchingSourcePropertyBindingSide, matchingSourceFieldBindingSide);
            default:
                throw new IllegalArgumentException(String.format("Unknow member access type: %s",
                        destinationMemberAccessType));
        }
    }

    private List<BindingSide> getMatchingPropertyByName(
            final BeanInfo sourceBeanInfo,
            final String atDestinationName,
            final MemberAccessType destinationMemberAccessType) {
        Optional<PropertyDescriptor> exactMatchResult
                = Arrays.stream(sourceBeanInfo.getPropertyDescriptors())
                .filter(i -> i.getName().equalsIgnoreCase(atDestinationName))
                .findFirst();

        if (exactMatchResult.isPresent()) {
            List<BindingSide> result = new LinkedList<>();
            result.add(new PropertyBindingSide(exactMatchResult.get()));

            return result;
        }

        if (_flateningEnabled) {
            Optional<PropertyDescriptor> partiallyMatchResult
                    = Arrays.stream(sourceBeanInfo.getPropertyDescriptors())
                    .filter(i -> StringUtils.startsWithIgnoreCase(atDestinationName, i.getName()))
                    .sorted((x, y) -> y.getName().length() - x.getName().length())
                    .findFirst();

            if (partiallyMatchResult.isPresent()) {
                BindingSide firstBinding = new PropertyBindingSide(partiallyMatchResult.get());
                Class innerPropertyClass = firstBinding.getValueClass();

                return getInnerMatchingSourceMemberByName(
                        innerPropertyClass,
                        atDestinationName,
                        firstBinding,
                        destinationMemberAccessType);
            } else {
                return null;
            }
        }

        return null;
    }

    private List<BindingSide> getMatchingFieldByName(
            final Class sourceClass,
            final String atDestinationName,
            final MemberAccessType destinationMemberAccessType) {
        Optional<Field> exactMatchResult
                = getInstanceFields(sourceClass).stream()
                .filter(i -> i.getName().equalsIgnoreCase(atDestinationName))
                .findFirst();

        if (exactMatchResult.isPresent()) {
            List<BindingSide> result = new LinkedList<>();
            result.add(new FieldBindingSide(exactMatchResult.get()));

            return result;
        }

        if (_flateningEnabled) {
            Optional<Field> partiallyMatchResult
                    = getInstanceFields(sourceClass).stream()
                    .filter(i -> StringUtils.startsWithIgnoreCase(atDestinationName, i.getName()))
                    .sorted((x, y) -> y.getName().length() - x.getName().length())
                    .findFirst();

            if (partiallyMatchResult.isPresent()) {
                BindingSide firstBinding = new FieldBindingSide(partiallyMatchResult.get());
                Class innerPropertyClass = firstBinding.getValueClass();

                return getInnerMatchingSourceMemberByName(
                        innerPropertyClass,
                        atDestinationName,
                        firstBinding,
                        destinationMemberAccessType);
            } else {
                return null;
            }
        }

        return null;
    }

    private List<Field> getInstanceFields(final Class sourceClass) throws SecurityException {
        return Arrays.stream(sourceClass.getFields())
                .filter(i -> Modifier.isStatic(i.getModifiers()) == false)
                .collect(Collectors.toList());
    }

    private List<BindingSide> getInnerMatchingSourceMemberByName(
            final Class innerPropertyClass,
            final String atDestinationName,
            final BindingSide firstBinding,
            final MemberAccessType destinationMemberAccessType)
            throws MappingException {
        BeanInfo innerPropertyBeanInfo;

        try {
            innerPropertyBeanInfo = Introspector.getBeanInfo(innerPropertyClass);
        } catch (IntrospectionException ex) {
            throw new MappingException(
                    String.format("Failed to get bean info for %s", innerPropertyClass), ex);
        }

        String innerDestinationName
                = atDestinationName.substring(firstBinding.getName().length());

        List<BindingSide> result
                = getMatchingSourceMemberByName(
                        innerPropertyBeanInfo,
                        innerPropertyClass,
                        innerDestinationName,
                        destinationMemberAccessType);

        if (result != null) {
            result.add(0, firstBinding);

            return result;
        } else {
            return null;
        }
    }

    private Binding getBindingIfAvailable(
            final Class sourceClass,
            final Class destinationClass,
            final MappingInfo mappingsInfo,
            final BindingSide[] sourceBindingSide,
            final BindingSide destinationBindingSide) {
        Class sourceValueClass = sourceBindingSide[sourceBindingSide.length - 1].getValueClass();
        Class destinationValueClass = destinationBindingSide.getValueClass();

        if (sourceValueClass.equals(destinationValueClass)) {
            return new Binding(sourceBindingSide, destinationBindingSide);
        } else {
            if (sourceClass.equals(sourceValueClass) && destinationClass.equals(destinationValueClass)) {
                return new BindingWithValueMap(sourceBindingSide, destinationBindingSide);
            } else if (mappingsInfo.isConverterAvailable(sourceValueClass, destinationValueClass)) {
                return new BindingWithValueConversion(sourceBindingSide, destinationBindingSide);
            } else if (mappingsInfo.isMapAvailable(sourceValueClass, destinationValueClass)) {
                return new BindingWithValueMap(sourceBindingSide, destinationBindingSide);
            } else if (destinationValueClass.isAssignableFrom(sourceValueClass)) {
                return new Binding(sourceBindingSide, destinationBindingSide);
            } else {
                return null;
            }
        }
    }

    private static List<Predicate<String>> toPredicates(final String[] members) {
        return Arrays.stream(members)
                .map(i -> Pattern.compile(i, Pattern.CASE_INSENSITIVE).asPredicate())
                .collect(Collectors.toList());
    }
}
