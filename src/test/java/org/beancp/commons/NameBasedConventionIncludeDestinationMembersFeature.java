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

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class NameBasedConventionIncludeDestinationMembersFeature {

    public static class SampleSource {

        private int _abc;

        private int _abcd;

        public int abc;

        public int abcd;

        private int _xyz;

        private int _efg;

        public int xyz;

        public int efg;

        public int getAbc() {
            return _abc;
        }

        public void setAbc(int _abc) {
            this._abc = _abc;
        }

        public int getAbcd() {
            return _abcd;
        }

        public void setAbcd(int _abcd) {
            this._abcd = _abcd;
        }

        public int getXyz() {
            return _xyz;
        }

        public void setXyz(int _xyz) {
            this._xyz = _xyz;
        }

        public int getEfg() {
            return _efg;
        }

        public void setEfg(int _efg) {
            this._efg = _efg;
        }
    }

    public static class SampleDestination {

        private int _abc;

        private int _abcd;

        public int abc;

        public int abcd;

        private int _xyz;

        private int _efg;

        public int xyz;

        public int efg;

        public int getAbc() {
            return _abc;
        }

        public void setAbc(int _abc) {
            this._abc = _abc;
        }

        public int getAbcd() {
            return _abcd;
        }

        public void setAbcd(int _abcd) {
            this._abcd = _abcd;
        }

        public int getXyz() {
            return _xyz;
        }

        public void setXyz(int _xyz) {
            this._xyz = _xyz;
        }

        public int getEfg() {
            return _efg;
        }

        public void setEfg(int _efg) {
            this._efg = _efg;
        }
    }

    @Test
    public void when_includeDestinationMembers_option_is_used_only_match_inclusion_regex_members_should_be_mapped() {
        // GIVEN
        SampleSource sourceObject = new SampleSource();
        sourceObject.abc = 1;
        sourceObject.abcd = 2;
        sourceObject.efg = 3;
        sourceObject.xyz = 4;
        sourceObject.setAbc(5);
        sourceObject.setAbcd(6);
        sourceObject.setEfg(7);
        sourceObject.setXyz(8);

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addMap(SampleSource.class, SampleDestination.class, (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().includeDestinationMembers("abc*", "X?Z"))
                ).buildMapper();

        SampleDestination result = mapper.map(sourceObject, SampleDestination.class);

        // THEN
        // included members
        assertEquals("Invalid result.abc value", sourceObject.abc, result.abc);
        assertEquals("Invalid result.abcd value", sourceObject.abcd, result.abcd);
        assertEquals("Invalid result.getAbc() value", sourceObject.getAbc(), result.getAbc());
        assertEquals("Invalid result.getAbcd() value", sourceObject.getAbcd(), result.getAbcd());
        assertEquals("Invalid result.xyz value", sourceObject.xyz, result.xyz);
        assertEquals("Invalid result.getXyz() value", sourceObject.getXyz(), result.getXyz());

        // excluded members
        assertEquals("Invalid result.efg value", 0, result.efg);
        assertEquals("Invalid result.getEfg() value", 0, result.getEfg());
    }
}
