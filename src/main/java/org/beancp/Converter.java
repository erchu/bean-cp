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
import static org.beancp.Util.failIfNull;

final class Converter<S, D> extends MappingExecutor<S, D> {

    private final Class<S> sourceClass;

    private final Class<D> destinationClass;

    private final TriConsumer<Mapper, S, D> convertionAction;

    public Converter(final Class<S> sourceClass, final Class<D> destinationClass,
            final TriConsumer<Mapper, S, D> convertionAction,
            final Supplier<D> destinationObjectBuilder) {
        failIfNull(sourceClass, "sourceClass");
        failIfNull(destinationClass, "destinationClass");
        failIfNull(convertionAction, "convertionAction");

        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
        this.convertionAction = convertionAction;

        if (destinationObjectBuilder != null) {
            setDestinationObjectBuilder(destinationObjectBuilder);
        }
    }

    public Converter(final Class<S> source, final Class<D> destination,
            final TriConsumer<Mapper, S, D> convertionAction) {
        this(source, destination, convertionAction, null);
    }

    @Override
    public D execute(Mapper caller, S source, Class<D> destinationClass) {
        D destination = constructDestinationObject(destinationClass);

        execute(caller, source, destination);

        return destination;
    }

    @Override
    public void execute(Mapper caller, S source, D destination) {
        convertionAction.accept(caller, source, destination);
    }

    @Override
    public Class<D> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public Class<S> getSourceClass() {
        return sourceClass;
    }
}
