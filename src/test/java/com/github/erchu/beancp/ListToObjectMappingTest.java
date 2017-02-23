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
package com.github.erchu.beancp;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rach
 */
public class ListToObjectMappingTest {

    public static class Address {

        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class Person {

        private String name;

        private final List<Address> addresses;

        public Person() {
            this.addresses = new LinkedList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Address> getAddresses() {
            return addresses;
        }
    }

    public static class Employee {

        private String name;

        private String state;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    @Test
    public void can_map_list_to_object() {
        // GIVEN
        Person person = new Person();
        person.setName("Manik");

        Address address = new Address();
        address.setState("CHD");
        person.getAddresses().add(address);

        Mapper mapper
                = new MapperBuilder()
                .addMap(Person.class,
                        Employee.class,
                        (conf, source, destination) -> conf
                        .bind(source::getName, destination::setName)
                        .bind(
                                () -> (source.getAddresses().size() > 0 ? source.getAddresses().get(0).getState() : null),
                                destination::setState))
                .buildMapper();

        // WHEN
        Employee employee = mapper.map(person, Employee.class);

        // THEN
        assertEquals("Invalid name", person.getName(), employee.getName());
        assertEquals("Invalid state", address.getState(), employee.getState());
    }
}
