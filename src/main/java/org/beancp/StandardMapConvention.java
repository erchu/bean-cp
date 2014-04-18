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
package org.beancp;

/**
 * Standard mapping conventions provided by bean-cp library.
 */
public class StandardMapConvention implements MapConvention {

    private String[] includeDestinationMembers;

    private String[] excludeDestinationMembers;

    private boolean flateningEnabled;

    private boolean failIfNotAllDestinationMembersMapped;

    private boolean failIfNotAllSourceMembersMapped;

    private boolean castOrMapIfPossible;

    /**
     * Constructs instance.
     */
    protected StandardMapConvention() {
    }

    /**
     * Returns default mapping convention:
     * <ul>
     * <li>No destination members excludes</li>
     * <li>Maximum possible number of destination members included</li>
     * <li>Will <b>not</b> fail if not all <b>destination</b> members are mapped</li>
     * <li>Will <b>not</b> fail if not all <b>source</b> members are mapped</li>
     * <li>Flatening feature disabled</li>
     * <li>Will cast or map if possible for members of different data type (see
     * {@link #castOrMapIfPossible()} method)</li>
     * </ul>
     *
     * @return default mapping convention.
     */
    public static StandardMapConvention getDefault() {
        StandardMapConvention defaultConvention = new StandardMapConvention();
        defaultConvention.excludeDestinationMembers = new String[0];
        defaultConvention.includeDestinationMembers = new String[0];
        defaultConvention.failIfNotAllDestinationMembersMapped = false;
        defaultConvention.failIfNotAllSourceMembersMapped = false;
        defaultConvention.flateningEnabled = false;
        defaultConvention.castOrMapIfPossible = true;

        return defaultConvention;
    }

    /**
     * Sets list of destination members which will be included by convention. Each member must be
     * regular expression matching field name or bean property name (according to beans
     * specification). If <b>not specified</b> (empty array) all subject to map by convention. If
     * <b>specified</b> (not empty array) only members with names matching any of {@code members}
     * could be mapped by convention. This list has lower priority that exclude list specified by
     * {@link #excludeDestinationMembers(java.lang.String...)} method.
     *
     * <p>
     * Note that when you put some member on list then it is not guaranteed that it will be mapped -
     * it still have to have matching source's member according to convention configuration.
     * </p>
     *
     * @param members members to include
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention includeDestinationMembers(String... members) {
        if (members == null) {
            throw new NullParameterException("members");
        }

        this.includeDestinationMembers = members;

        return this;
    }

    /**
     * Sets list of destination members which will be excluded (ignored) by convention. Each member
     * must be regular expression matching field name or bean property name (according to beans
     * specification). This list has higher priority that include list specified by {@link #includeDestinationMembers(java.lang.String...)
     * } method.
     *
     * <p>
     * Note that when you put some member on list then it is not guaranteed that it will be mapped -
     * it still have to have matching source's member according to convention configuration.
     * </p>
     *
     * @param members members to ignore
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention excludeDestinationMembers(String... members) {
        if (members == null) {
            throw new NullParameterException("members");
        }

        this.excludeDestinationMembers = members;

        return this;
    }

    /**
     * Enables flattening feature. This feature will try to match members from inner classes only if
     * no direct member can be matched. This is useful if you have complex model to be mapped to
     * simpler one. Destination member will be matched to source inner class member if destination
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
    public StandardMapConvention enableFlattening() {
        this.flateningEnabled = true;

        return this;
    }

    /**
     * Disables flattening feature as described in {@link #enableFlattening()} method. This is
     * opposite to {@link #enableFlattening()} method.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention disableFlattening() {
        this.flateningEnabled = false;

        return this;
    }

    /**
     * Will fail during mapping if not all destination properties are mapped.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention failIfNotAllDestinationMembersMapped() {
        this.failIfNotAllDestinationMembersMapped = true;

        return this;
    }

    /**
     * Will fail during mapping if not all source properties are mapped.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention failIfNotAllSourceMembersMapped() {
        this.failIfNotAllSourceMembersMapped = true;

        return this;
    }

    /**
     * For matching members names, but of different data types will try cast or map members wherever
     * it is possible. If matching members are of different types mapper will try one of the
     * following techniques to perform mapping:
     * <ul>
     * <li>Cast operation between primitive types</li>
     * <li>Converting values to String</li>
     * <li>Parsing String values for primitive types</li>
     * <li>Mapping types using available mapper</li>
     * </ul>
     *
     * <p>
     * This option is opposite to {@link #mapOnlySameTypeMembers()}.
     * </p>
     *
     * <p>
     * Note than invalid value can result in exception during mapping. For example when parsing
     * String to int mapping will fail if string value is not valid number.
     * </p>
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention castOrMapIfPossible() {
        this.castOrMapIfPossible = true;

        return this;
    }

    /**
     * Members will be not mapped if are not of the same data type.
     *
     * <p>
     * This option is opposite to {@link #castOrMapIfPossible()}.
     * </p>
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention mapOnlySameTypeMembers() {
        this.castOrMapIfPossible = false;

        return this;
    }

    @Override
    public void build(final Mapper mapper, final Class sourceClass, final Class destinationClass) {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute(final Mapper mapper, final Object source, final Object destination) {
        //TODO: Not supported yet
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
