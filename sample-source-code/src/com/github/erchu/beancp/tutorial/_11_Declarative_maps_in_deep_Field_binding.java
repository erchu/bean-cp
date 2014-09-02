package com.github.erchu.beancp.tutorial;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects1.Logger;
import com.github.erchu.beancp.tutorial.objects1.Order;
import com.github.erchu.beancp.tutorial.objects1.OrderOverviewDto;
import com.github.erchu.beancp.tutorial.objects3.SampleDestination;
import com.github.erchu.beancp.tutorial.objects3.SampleSource;

public class _11_Declarative_maps_in_deep_Field_binding {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				SampleSource.class,
				SampleDestination.class,
				(conf, source, destination) -> conf
						.bind(() -> { return source.sourceMember; }, v -> { destination.destinationMember = v; })
					).buildMapper();

		SampleSource source = new SampleSource();
		source.sourceMember = 8;

		// WHEN
		SampleDestination destination = mapper.map(source, SampleDestination.class);

		// THEN
		assertEquals(source.sourceMember, destination.destinationMember);
	}
}
