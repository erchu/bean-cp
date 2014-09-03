package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.CollectionConverters;

public class _13_Converters_Collection_converters {

	@Test
	public void test() {
        // GIVEN
        long[] sourceInstance = new long[] { 1, 2, 3 };

        // WHEN
        Mapper mapper = new MapperBuilder()
                .addConverter(CollectionConverters.getArrayToCollection(long.class))
                .buildMapper();

        Collection<?> result = mapper.map(sourceInstance, Collection.class);

        // THEN
        assertEquals(
                "Invalid result size",
                sourceInstance.length,
                result.size());

        for (long j : sourceInstance) {
            assertTrue(
                    "Missing element " + j + " in collection",
                    result.contains(j));
        }
	}
}
