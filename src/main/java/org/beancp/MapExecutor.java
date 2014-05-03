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

import java.util.function.Supplier;
import static org.apache.commons.lang3.Validate.*;

abstract class MapExecutor<S, D> {

    private Supplier<D> destinationObjectBuilder;

    abstract void execute(final Mapper caller, final S source, final D destination);

    abstract Class<D> getDestinationClass();

    abstract Class<S> getSourceClass();

    protected Supplier<D> getDestinationObjectBuilder() {
        return destinationObjectBuilder;
    }

    protected void setDestinationObjectBuilder(final Supplier<D> destinationObjectBuilder) {
        notNull(destinationObjectBuilder, "destinationObjectBuilder");

        this.destinationObjectBuilder = destinationObjectBuilder;
    }
}
