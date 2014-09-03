package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;

public class _12_Converters_When_and_how {

	@Test
	@SuppressWarnings("deprecation")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder()
			.addConverter(Date.class, LocalDate.class, source -> {
					Instant instant = source.toInstant();
					ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
					LocalDate result = zonedDateTime.toLocalDate();

					return result;
				}).buildMapper();

		Date now = new Date();

		// WHEN
		LocalDate destination = mapper.map(now, LocalDate.class);

		// THEN
		assertEquals(now.getYear() + 1900, destination.getYear());
		assertEquals(now.getMonth() + 1, destination.getMonthValue());
		assertEquals(now.getDay(), destination.getDayOfMonth());
	}
}
