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
package com.github.erchu.beancp.integration_tests;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.NameBasedMapConvention;
import static org.junit.Assert.*;
import org.junit.Test;

public class RecursiveStructureMappingTest {

    public static class SourceTreeNodeWrapper {

        private final SourceTreeNode node;

        public SourceTreeNodeWrapper(SourceTreeNode node) {
            if (node == null) {
                throw new NullPointerException("Null not allowed for 'node' parameter.");
            }

            this.node = node;
        }

        public SourceTreeNode getNode() {
            return node;
        }
    }

    public static class SourceTreeNode {

        private SourceTreeNode left;

        private SourceTreeNode right;

        private String name;

        private SourceTreeNodeWrapper wrapped;

        public SourceTreeNode getLeft() {
            return left;
        }

        public void setLeft(SourceTreeNode left) {
            this.left = left;
        }

        public SourceTreeNode getRight() {
            return right;
        }

        public void setRight(SourceTreeNode right) {
            this.right = right;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SourceTreeNodeWrapper getWrapped() {
            return wrapped;
        }

        public void setWrapped(SourceTreeNodeWrapper wrapped) {
            this.wrapped = wrapped;
        }
    }

    public static class DestinationTreeNode {

        private DestinationTreeNode left;

        private DestinationTreeNode right;

        private DestinationTreeNode wrappedNode;

        private String name;

        public DestinationTreeNode getLeft() {
            return left;
        }

        public void setLeft(DestinationTreeNode left) {
            this.left = left;
        }

        public DestinationTreeNode getRight() {
            return right;
        }

        public void setRight(DestinationTreeNode right) {
            this.right = right;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DestinationTreeNode getWrappedNode() {
            return wrappedNode;
        }

        public void setWrappedNode(DestinationTreeNode wrappedNode) {
            this.wrappedNode = wrappedNode;
        }
    }

    @Test
    public void map_any_name_based_convention_should_map_recursive_structures() {
        // GIVEN
        SourceTreeNode top = getSampleSourceData();

        Mapper mapper = new MapperBuilder()
                .addMapAnyByConvention(NameBasedMapConvention.get().enableFlattening())
                .buildMapper();

        // WHEN
        DestinationTreeNode result = mapper.map(top, DestinationTreeNode.class);

        // THEN
        assertTreeEquals(top, result);
    }

    @Test
    public void name_based_convention_should_map_recursive_structures() {
        // GIVEN
        SourceTreeNode top = getSampleSourceData();

        Mapper mapper = new MapperBuilder()
                .addMap(SourceTreeNode.class, DestinationTreeNode.class,
                        (config, source, destination)
                        -> config.useConvention(NameBasedMapConvention.get().enableFlattening())
                ).buildMapper();

        // WHEN
        DestinationTreeNode result = mapper.map(top, DestinationTreeNode.class);

        // THEN
        assertTreeEquals(top, result);
    }

    @Test
    public void declarative_map_should_map_recursive_structures() {
        // GIVEN
        SourceTreeNode top = getSampleSourceData();

        Mapper mapper = new MapperBuilder()
                .addMap(SourceTreeNode.class, DestinationTreeNode.class,
                        (config, source, destination)
                        -> config.bind(source::getName, destination::setName)
                        .mapInner(source::getLeft, destination::setLeft, DestinationTreeNode.class)
                        .mapInner(source::getRight, destination::setRight, DestinationTreeNode.class)
                        .mapInner(
                                () -> source.getWrapped() != null ? source.getWrapped().getNode() : null,
                                destination::setWrappedNode,
                                DestinationTreeNode.class)
                ).buildMapper();

        // WHEN
        DestinationTreeNode result = mapper.map(top, DestinationTreeNode.class);

        // THEN
        assertTreeEquals(top, result);
    }

    @Test
    public void converter_should_map_recursive_structures() {
        // GIVEN
        SourceTreeNode top = getSampleSourceData();

        Mapper mapper = new MapperBuilder()
                .addConverter(SourceTreeNode.class, DestinationTreeNode.class,
                        (mapperRef, source) -> {
                            DestinationTreeNode destination = new DestinationTreeNode();
                            destination.setName(source.getName());

                            if (source.getLeft() != null) {
                                destination.setLeft(mapperRef.map(source.getLeft(), DestinationTreeNode.class));
                            }

                            if (source.getRight() != null) {
                                destination.setRight(mapperRef.map(source.getRight(), DestinationTreeNode.class));
                            }

                            if (source.getWrapped() != null) {
                                destination.setWrappedNode(mapperRef.map(source.getWrapped().getNode(), DestinationTreeNode.class));
                            }

                            return destination;
                        }).buildMapper();

        // WHEN
        DestinationTreeNode result = mapper.map(top, DestinationTreeNode.class);

        // THEN
        assertTreeEquals(top, result);
    }

    private SourceTreeNode getSampleSourceData() {
        SourceTreeNode top = new SourceTreeNode();
        top.setName("top");

        SourceTreeNode wrappedFirstLevel = new SourceTreeNode();
        wrappedFirstLevel.setName("wrappedFirstLevel");
        top.setWrapped(new SourceTreeNodeWrapper(wrappedFirstLevel));

        SourceTreeNode leftFirstLevel = new SourceTreeNode();
        leftFirstLevel.setName("leftFirstLevel");
        top.setLeft(leftFirstLevel);

        SourceTreeNode rightFirstLevel = new SourceTreeNode();
        rightFirstLevel.setName("rightFirstLevel");
        top.setRight(rightFirstLevel);

        SourceTreeNode leftSecondLevel = new SourceTreeNode();
        leftSecondLevel.setName("leftSecondLevel");
        leftFirstLevel.setLeft(leftSecondLevel);

        SourceTreeNode rightThirdLevel = new SourceTreeNode();
        rightThirdLevel.setName("rightThirdLevel");
        leftSecondLevel.setRight(rightThirdLevel);

        return top;
    }

    private void assertTreeEquals(final SourceTreeNode source, final DestinationTreeNode destination) {
        assertEquals("Invalid node name property value.", source.getName(), destination.getName());
        assertTrue("Invalid left node value.", ((source.getLeft() == null) == (destination.getLeft() == null)));
        assertTrue("Invalid right node value.", ((source.getRight() == null) == (destination.getRight() == null)));
        assertTrue("Invalid wrapped node value.", ((source.getWrapped() == null) == (destination.getWrappedNode() == null)));

        if (source.getLeft() != null) {
            assertTreeEquals(source.getLeft(), destination.getLeft());
        }

        if (source.getRight() != null) {
            assertTreeEquals(source.getRight(), destination.getRight());
        }

        if (source.getWrapped() != null) {
            assertTreeEquals(source.getWrapped().getNode(), destination.getWrappedNode());
        }
    }
}
