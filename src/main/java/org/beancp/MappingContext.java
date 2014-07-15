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

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import static org.apache.commons.lang3.Validate.*;

class MappingContext implements AutoCloseable {

    private static class MappingRequest {

        private final Object _source;

        private final Class _resultClass;

        public MappingRequest(final Object source, final Class result) {
            _source = source;
            _resultClass = result;
        }

        public Object getSource() {
            return _source;
        }

        public Class getResultClass() {
            return _resultClass;
        }

        @Override
        public int hashCode() {
            return _resultClass.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            
            if (getClass() != obj.getClass()) {
                return false;
            }
            
            final MappingRequest other = (MappingRequest) obj;
            
            if (this._source != other._source) {
                return false;
            }
            
            if (Objects.equals(this._resultClass, other._resultClass) == false) {
                return false;
            }
            
            return true;
        }
    }

    private final static ThreadLocal<java.util.Map<MappingRequest, Object>> _mappingResults
            = new ThreadLocal<>();

    private final boolean iAmRootContext;

    public MappingContext() {
        if (_mappingResults.get() == null) {
            iAmRootContext = true;
            _mappingResults.set(new HashMap<>());
        } else {
            iAmRootContext = false;
        }
    }

    public Optional<Object> getMappingResult(final Object source, final Class resultClass) {
        notNull(source, "source");
        notNull(resultClass, "resultClass");

        Object result = _mappingResults.get().get(new MappingRequest(source, resultClass));

        return (result != null) ? Optional.of(result) : Optional.empty();
    }

    public void addMappingResult(
            final Object source, final Object result, final Class resultClass) {
        notNull(source, "source");
        notNull(result, "result");
        notNull(resultClass, "resultClass");

        _mappingResults.get().put(new MappingRequest(source, resultClass), result);
    }

    public void removeMappingResult(final Object source, final Class resultClass) {
        notNull(source, "source");
        notNull(resultClass, "resultClass");

        _mappingResults.get().remove(new MappingRequest(source, resultClass));
    }

    @Override
    public void close() throws Exception {
        if (iAmRootContext) {
            _mappingResults.set(null);
        }
    }
}
