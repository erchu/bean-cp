/*
 * ObjectMapper4j
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
package org.objectmapper4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;


//TODO: Test is not complete, only basic scenario (proof of concept) implemented
public class FromPropertyBindingTest {

    public static class SimpleSource {

        private String x;

        private String y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }


    public static class SimpleDestination {

        private String a;

        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

    @Test
    public void mapper_should_be_able_to_extract_from_property_binding_information() throws NoSuchMethodException {
        // WHEN
        MapperBuilder mapperBuilder = new MapperBuilder();

        MapDefinition<SimpleSource, SimpleDestination> map
                = mapperBuilder.addMap(SimpleSource.class, SimpleDestination.class)
                .bindFromMember(
                        source -> source.getX(),
                        (destination, value) -> destination.setA(value))
                .bindFromMember(
                        source -> source.getY(),
                        (destination, value) -> destination.setB(value));

        // THEN
        List<Binding> bindings = map.getBindings();

        // Number of bindings
        assertEquals("Invalid number of bindings.", 2, bindings.size());

        // 'a' property binding
        Method xProperty = SimpleSource.class.getMethod("getX");
        Method aProperty = SimpleDestination.class.getMethod("setA", String.class);

        assertEquals("Invalid number of 'a' property bindings.",
                1,
                getPropertyBindingStream(bindings, aProperty).count());

        Binding aBiding = getPropertyBindingStream(bindings, aProperty).findFirst().get();

        assertEquals("Invalid binding type for 'a' property binding.", FromPropertyBinding.class, aBiding.getClass());
        assertEquals("Invalid source for 'a' property binding.", xProperty, ((FromPropertyBinding) aBiding).getGetter());

        // 'b' property binding
        Method yProperty = SimpleSource.class.getMethod("getY");
        Method bProperty = SimpleDestination.class.getMethod("setB", String.class);

        assertEquals("Invalid number of 'a' property bindings.",
                1,
                getPropertyBindingStream(bindings, bProperty).count());

        Binding bBiding = getPropertyBindingStream(bindings, bProperty).findFirst().get();

        assertEquals("Invalid binding type for 'a' property binding.", FromPropertyBinding.class, bBiding.getClass());
        assertEquals("Invalid source for 'a' property binding.", yProperty, ((FromPropertyBinding) bBiding).getGetter());
    }

    private Stream<Binding> getPropertyBindingStream(List<Binding> bindings, Method aProperty) {
        return bindings.stream().filter(n -> n.getSetter().equals(aProperty));
    }
}
