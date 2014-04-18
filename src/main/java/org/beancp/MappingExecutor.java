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

abstract class MappingExecutor<S, D> {

    private Supplier<D> destinationObjectBuilder;

    abstract D execute(final Mapper caller, final S source, final Class<D> destinationClass);

    abstract void execute(final Mapper caller, final S source, final D destination);

    abstract Class<D> getDestinationClass();

    abstract Class<S> getSourceClass();

    protected Supplier<D> getDestinationObjectBuilder() {
        return destinationObjectBuilder;
    }

    protected void setDestinationObjectBuilder(final Supplier<D> destinationObjectBuilder) {
        if (destinationObjectBuilder == null) {
            throw new NullParameterException("destinationObjectBuilder");
        }
        
        this.destinationObjectBuilder = destinationObjectBuilder;
    }

    protected D constructDestinationObject(final Class<D> destinationClass) throws MappingException {
        try {
            D destination;

            if (destinationObjectBuilder != null) {
                destination = destinationObjectBuilder.get();
                if (destinationClass.isAssignableFrom(destination.getClass()) == false) {
                    throw new MappingException(String.format("Destination object class %s returned "
                            + "by constructDestinationObjectUsing cannot be assigned to expected "
                            + "class %s.", destination.getClass(), destinationClass));
                }
            } else {
                destination = (D) destinationClass.newInstance();
            }

            return destination;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new MappingException("Cannot create destination instance.", ex);
        }
    }
}
