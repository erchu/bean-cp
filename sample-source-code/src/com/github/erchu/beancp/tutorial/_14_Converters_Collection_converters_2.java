package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.CollectionConverters;

public class _14_Converters_Collection_converters_2 {

	@Test
	public void test() {
		// GIVEN

		// WHEN
		Mapper mapper = new MapperBuilder().addConverter(
				CollectionConverters.getCollectionToArray(String.class))
				.buildMapper();

		Collection<String> collectionInstance = new LinkedList<>();
		collectionInstance.add("1");
		collectionInstance.add("2");
		collectionInstance.add("3");

		String[] result = mapper.map(collectionInstance, String[].class);

		// THEN
		String[] testValues = collectionInstance.toArray(new String[collectionInstance.size()]);
		
		assertEquals("Invalid result size", testValues.length, result.length);

		for (String j : testValues) {
			assertTrue("Missing element " + j + " in collection",
					Arrays.stream(result).anyMatch(k -> k.equals(j)));
		}
	}
}
