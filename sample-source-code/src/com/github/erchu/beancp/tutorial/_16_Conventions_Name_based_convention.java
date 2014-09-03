package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.NumberConverters;

public class _16_Conventions_Name_based_convention {

	@Test
	public void test() {
		// GIVEN

		// WHEN
		Mapper mapper = new MapperBuilder()
			.addConverter(NumberConverters.get())
			.buildMapper();

		Double result = mapper.map(1l, Double.class);

		// THEN
		assertEquals(Double.valueOf(1.0), result);
	}
}
