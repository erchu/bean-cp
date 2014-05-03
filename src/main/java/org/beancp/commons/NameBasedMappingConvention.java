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

import org.beancp.MappingConvention;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.beancp.Mapper;
import org.beancp.MappingException;
import org.beancp.MappingInfo;
import static org.apache.commons.lang3.ObjectUtils.*;
import static org.apache.commons.lang3.Validate.*;

/**
 * Convention matches fields by name.
 */
public class NameBasedMappingConvention implements MappingConvention {

    private enum MemberAccessType {

        FIELD,
        PROPERTY
    }

    private String[] includeDestinationMembers;

    private String[] excludeDestinationMembers;

    private boolean flateningEnabled;

    private boolean failIfNotAllDestinationMembersMapped;

    private boolean failIfNotAllSourceMembersMapped;

    private List<Binding> bindings = null;

    //TODO: Add clone controling options
    /**
     * Constructs instance.
     */
    protected NameBasedMappingConvention() {
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
    public static NameBasedMappingConvention get() {
        NameBasedMappingConvention defaultConvention = new NameBasedMappingConvention();
        defaultConvention.excludeDestinationMembers = new String[0];
        defaultConvention.includeDestinationMembers = new String[0];
        defaultConvention.failIfNotAllDestinationMembersMapped = false;
        defaultConvention.failIfNotAllSourceMembersMapped = false;
        defaultConvention.flateningEnabled = false;

        return defaultConvention;
    }

    /**
     * Sets list of destination members which will be included to matching. Each entry must be
     * regular expression matching field name or bean property name (according to beans
     * specification). If <b>not specified</b> (empty array) all members are subject to map by
     * convention. If <b>specified</b> (not empty array) only members with names matching any of
     * {@code members} could be mapped by convention. This list has lower priority that exclude list
     * specified by {@link #excludeDestinationMembers(java.lang.String...)} method.
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
    public NameBasedMappingConvention includeDestinationMembers(String... members) {
        notNull(members, "members");

        this.includeDestinationMembers = members;

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
    public NameBasedMappingConvention excludeDestinationMembers(String... members) {
        notNull(members, "members");

        this.excludeDestinationMembers = members;

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
     *          return this.name;
     *     }
     * }
     *
     * class Order {
     *
     *     private Customer customer;
     *
     *     public Customer getCustomer() {
     *          return this.customer;
     *     }
     * }
     *
     * class OrderDto {
     *
     *     private String customerName;
     *
     *     public void setCustomerName(final String customerName) {
     *          this.customerName = customerName;
     *     }
     *
     *     public String getCustomerName() {
     *          return this.customerName;
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
    public NameBasedMappingConvention enableFlattening() {
        this.flateningEnabled = true;

        return this;
    }

    /**
     * Disables flattening feature as described in {@link #enableFlattening()} method. This is
     * opposite to {@link #enableFlattening()} method.
     *
     * @return this (for method chaining)
     */
    public NameBasedMappingConvention disableFlattening() {
        this.flateningEnabled = false;

        return this;
    }

    /**
     * Convention will fail during map building (see
     * {@link #build(org.beancp.MappingsInfo, java.lang.Class, java.lang.Class)} method) if not all
     * destination properties are mapped.
     *
     * @return this (for method chaining)
     */
    public NameBasedMappingConvention failIfNotAllDestinationMembersMapped() {
        this.failIfNotAllDestinationMembersMapped = true;

        return this;
    }

    /**
     * Convention will fail during map building (see
     * {@link #build(org.beancp.MappingsInfo, java.lang.Class, java.lang.Class)} method) if not all
     * source properties are mapped.
     *
     * @return this (for method chaining)
     */
    public NameBasedMappingConvention failIfNotAllSourceMembersMapped() {
        this.failIfNotAllSourceMembersMapped = true;

        return this;
    }

    @Override
    public void build(final MappingInfo mappingInfo, final Class sourceClass, final Class destinationClass) {
        notNull(mappingInfo, "mappingInfo");
        notNull(sourceClass, "sourceClass");
        notNull(destinationClass, "destinationClass");

        //TODO: Proxy generation using javassist would be faster?
        this.bindings = getBindings(mappingInfo, sourceClass, destinationClass);
    }

    @Override
    public void map(final Mapper mapper, final Object source, final Object destination) {
        if (tryMap(mapper, source, destination) == false) {
            throw new MappingException(String.format("I don't know how to map %s to %s",
                    source.getClass(), destination.getClass()));
        }
    }

    @Override
    public boolean tryMap(final Mapper mapper, final Object source, final Object destination) {
        notNull(mapper, "mapper");
        notNull(source, "source");
        notNull(destination, "destination");

        List<Binding> bindingsToExecute = getBindingsToExecute(
                mapper, source.getClass(), destination.getClass());

        if (bindingsToExecute.isEmpty()) {
            return false;
        } else {
            executeBindings(bindingsToExecute, mapper, source, destination);

            return true;
        }
    }

    @Override
    public boolean canMap(
            final MappingInfo mappingsInfo, final Class sourceClass, final Class destinationClass) {
        notNull(mappingsInfo, "mappingsInfo");
        notNull(sourceClass, "source");
        notNull(destinationClass, "destination");

        List<Binding> bindingsToExecute = getBindingsToExecute(
                mappingsInfo, sourceClass, destinationClass);

        return (bindingsToExecute.isEmpty() == false);
    }

    private List<Binding> getBindingsToExecute(
            final MappingInfo mappingsInfo, final Class sourceClass, final Class destinationClass) {
        // According to API specification build() method but never concurrently or after first of
        // this method, so we can safely get bindings field value without acquiring any locks or
        // defining fields as volatile.
        List<Binding> bindingsToExecute = (bindings != null)
                ? bindings
                // According to API specification it is build() method may be not executed before
                // this method call. In this situation we generate bindings on the fly. Moreover API
                // prohibits produce state that is shared state between calls, so next call
                : getBindings(mappingsInfo, sourceClass, destinationClass);

        return bindingsToExecute;
    }

    private void executeBindings(final List<Binding> bindingsToExecute, final Mapper mapper,
            final Object source, final Object destination) {
        bindingsToExecute.stream().forEach(i -> {
            i.execute(mapper, source, destination);
        });
    }

    private List<Binding> getBindings(
            final MappingInfo mappingsInfo,
            final Class sourceClass,
            final Class destinationClass) {
        if (excludeDestinationMembers.length > 0) {
            // TODO: Implement excludeDestinationMembers option support
            throw new UnsupportedOperationException("excludeDestinationMembers option not supported yet.");
        }

        if (includeDestinationMembers.length > 0) {
            // TODO: Implement includeDestinationMembers option support
            throw new UnsupportedOperationException("includeDestinationMembers option not supported yet.");
        }

        if (failIfNotAllDestinationMembersMapped) {
            // TODO: Implement failIfNotAllDestinationMembersMapped option support
            throw new UnsupportedOperationException("failIfNotAllDestinationMembersMapped option not supported yet.");
        }

        if (failIfNotAllSourceMembersMapped) {
            // TODO: Implement failIfNotAllDestinationMembersMapped option support
            throw new UnsupportedOperationException("failIfNotAllSourceMembersMapped option not supported yet.");
        }

        if (flateningEnabled) {
            // TODO: Implement flateningEnabled option support
            // TODO: Reverse flattening?
            throw new UnsupportedOperationException("flateningEnabled option not supported yet.");
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
                BindingSide sourceBindingSide
                        = getMatchingSourceMember(sourceBeanInfo, sourceClass,
                                destinationProperty.getName(), MemberAccessType.PROPERTY);

                if (sourceBindingSide != null) {
                    BindingSide destinationBindingSide
                            = new PropertyBindingSide(destinationProperty);

                    Binding binding = getBidingIfAvailable(
                            mappingsInfo, sourceBindingSide, destinationBindingSide);

                    if (binding != null) {
                        result.add(binding);
                    }
                }
            }
        }

        for (Field destinationMember : destinationClass.getFields()) {
            BindingSide sourceBindingSide = getMatchingSourceMember(sourceBeanInfo, sourceClass,
                    destinationMember.getName(), MemberAccessType.FIELD);

            if (sourceBindingSide != null) {
                BindingSide destinationBindingSide = new FieldBindingSide(destinationMember);
                Binding binding = getBidingIfAvailable(
                        mappingsInfo, sourceBindingSide, destinationBindingSide);

                if (binding != null) {
                    result.add(binding);
                }
            }
        }

        return result;
    }

    private BindingSide getMatchingSourceMember(
            final BeanInfo sourceBeanInfo,
            final Class sourceClass,
            final String atDestinationName,
            final MemberAccessType destinationMemberAccessType) {
        BindingSide matchingSourcePropertyBindingSide
                = getMatchingPropertyBindingSide(sourceBeanInfo, atDestinationName);

        BindingSide matchingSourceFieldBindingSide
                = getMatchingFieldBindingSide(sourceClass, atDestinationName);

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

    private PropertyBindingSide getMatchingPropertyBindingSide(
            final BeanInfo sourceBeanInfo,
            final String atDestinationName) {
        Optional<PropertyDescriptor> result
                = Arrays.stream(sourceBeanInfo.getPropertyDescriptors())
                .filter(i -> i.getName().equals(atDestinationName))
                .findFirst();

        return (result.isPresent()) ? new PropertyBindingSide(result.get()) : null;
    }

    private FieldBindingSide getMatchingFieldBindingSide(
            final Class sourceClass, final String atDestinationName) {
        Optional<Field> result
                = Arrays.stream(sourceClass.getFields())
                .filter(i -> i.getName().equals(atDestinationName))
                .findFirst();

        return (result.isPresent()) ? new FieldBindingSide(result.get()) : null;
    }

    private Binding getBidingIfAvailable(
            final MappingInfo mappingsInfo,
            final BindingSide sourceBindingSide,
            final BindingSide destinationBindingSide) {
        Class sourceValueClass = sourceBindingSide.getValueClass();
        Class destinationValueClass = destinationBindingSide.getValueClass();

        if (sourceValueClass.equals(destinationValueClass)) {
            return new Binding(sourceBindingSide, destinationBindingSide);
        } else {
            if (mappingsInfo.isMapperAvailable(sourceValueClass, destinationValueClass)) {
                return new BindingWithValueMapping(sourceBindingSide, destinationBindingSide);
            } else if (destinationValueClass.isAssignableFrom(sourceValueClass)) {
                return new Binding(sourceBindingSide, destinationBindingSide);
            } else {
                return null;
            }
        }
    }
}
