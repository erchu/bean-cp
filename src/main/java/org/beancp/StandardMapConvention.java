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
 * Map by convention configuration.
 */
public class StandardMapConvention implements MapConvention {
    
    private String[] includeDestinationMembers;
    
    private String[] excludeDestinationMembers;
    
    private boolean flateningEnabled;
    
    private boolean failIfNotAllDestinationMembersMapped;
    
    private boolean failIfNotAllSourceMembersMapped;

    /**
     * Constructs instance.
     */
    protected StandardMapConvention() {
    }

    /**
     * Returns default mapping convention:
     * <ul>
     *   <li>No destination members excludes</li>
     *   <li>Maximum possible number of destination members included</li>
     *   <li>Will <b>not</b> fail if not all <b>destination</b> members are mapped</li>
     *   <li>Will <b>not</b> fail if not all <b>source</b> members are mapped</li>
     *   <li>Flatening feature disabled</li>
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

        return defaultConvention;
    }

    /**
     * Sets list of destination members which will be included by convention. If not specified all
     * members are included by default.
     *
     * @param members members to include
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention includeDestinationMembers(String... members) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Sets list of destination members which will be ignored (not mapped) by convention.
     *
     * @param members members to ignore
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention excludeDestinationMembers(String... members) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Enables flatening feature.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention enableFlatening() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Disables flatening feature.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention disableFlatening() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Will fail during mapping if not all destination properties are mapped.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention failIfNotAllDestinationMembersMapped() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Will fail during mapping if not all source properties are mapped.
     *
     * @return this (for method chaining)
     */
    public StandardMapConvention failIfNotAllSourceMembersMapped() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void build(Mapper mapper, Class sourceClass, Class destinationClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute(Mapper mapper, Object source, Object destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
