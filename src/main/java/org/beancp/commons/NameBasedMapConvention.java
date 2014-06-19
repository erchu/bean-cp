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

import org.beancp.FieldBindingSide;
import org.beancp.PropertyBindingSide;
import org.beancp.Binding;
import org.beancp.BindingWithValueConversion;
import org.beancp.BindingWithValueMap;
import org.beancp.BindingSide;
import org.beancp.MapConvention;
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
import org.beancp.MappingException;
import org.beancp.MappingInfo;
import static org.apache.commons.lang3.ObjectUtils.*;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.Validate.*;

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
     * Constructs instance.
     */
    protected NameBasedMapConvention() {
    }

    /**
     * Returns mapping convention with following configuration
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
     * Sets list of destination members which will be included to matching. Each entry must be
     * regular expression matching field name or bean property name (according to beans
     * specification). If <b>not specified</b> (empty array) all members are subject to map by
     * convention. If <b>specified</b> (not empty array) only members with names matching any of
     * {@code members} could be mapped by convention ignoring case. This list has lower priority
     * that exclude list specified by {@link #excludeDestinationMembers(java.lang.String...)}
     * method.
     *
     * <p>
     * Note that when you put some member on list then it is not guaranteed that it will be mapped
     * &#8212; it still have to have matching source's member according to convention configuration.
     * </p>
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
     * must be regular expression matching field name or bean property name (according to beans
     * specification). This list has higher priority that include list specified by {@link #includeDestinationMembers(java.lang.String...)
     * } method.
     *
     * <p>
     * Note that when you put some member on list then it is not guaranteed that it will be mapped
     * &#8212; it still have to have matching source's member according to convention configuration.
     * </p>
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
     * member's name parts match path to source member. For example: for below classes
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
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} method) if not all
     * destination properties are mapped.
     *
     * @return this (for method chaining)
     */
    public NameBasedMapConvention failIfNotAllDestinationMembersMapped() {
        _failIfNotAllDestinationMembersMapped = true;

        return this;
    }

    /**
     * Convention will fail during map building (see
     * {@link #build(org.beancp.MappingInfo, java.lang.Class, java.lang.Class)} method) if not all
     * source properties are mapped.
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
        if (_failIfNotAllDestinationMembersMapped) {
            // TODO: Implement failIfNotAllDestinationMembersMapped option support
            throw new UnsupportedOperationException("failIfNotAllDestinationMembersMapped option not supported yet.");
        }

        if (_failIfNotAllSourceMembersMapped) {
            // TODO: Implement failIfNotAllDestinationMembersMapped option support
            throw new UnsupportedOperationException("failIfNotAllSourceMembersMapped option not supported yet.");
        }

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

                    Binding binding = getBidingIfAvailable(
                            mappingsInfo, sourceBindingSideArray, destinationBindingSide);

                    if (binding != null) {
                        result.add(binding);
                    }
                }
            }
        }

        for (Field destinationMember : destinationClass.getFields()) {
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

                Binding binding = getBidingIfAvailable(
                        mappingsInfo, sourceBindingSideArray, destinationBindingSide);

                if (binding != null) {
                    result.add(binding);
                }
            }
        }

        return result;
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
                = Arrays.stream(sourceClass.getFields())
                .filter(i -> i.getName().equalsIgnoreCase(atDestinationName))
                .findFirst();

        if (exactMatchResult.isPresent()) {
            List<BindingSide> result = new LinkedList<>();
            result.add(new FieldBindingSide(exactMatchResult.get()));

            return result;
        }

        if (_flateningEnabled) {
            Optional<Field> partiallyMatchResult
                    = Arrays.stream(sourceClass.getFields())
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

    private Binding getBidingIfAvailable(
            final MappingInfo mappingsInfo,
            final BindingSide[] sourceBindingSide,
            final BindingSide destinationBindingSide) {
        Class sourceValueClass = sourceBindingSide[sourceBindingSide.length - 1].getValueClass();
        Class destinationValueClass = destinationBindingSide.getValueClass();

        if (sourceValueClass.equals(destinationValueClass)) {
            return new Binding(sourceBindingSide, destinationBindingSide);
        } else {
            if (mappingsInfo.isConverterAvailable(sourceValueClass, destinationValueClass)) {
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
