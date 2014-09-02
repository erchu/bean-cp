package com.github.erchu.beancp.tutorial;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects1.Logger;
import com.github.erchu.beancp.tutorial.objects1.Order;
import com.github.erchu.beancp.tutorial.objects1.OrderOverviewDto;

public class _10_Declarative_maps_in_deep_Before_and_after_map_plus_mapper_reference {

	@Test
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Order.class,
				OrderOverviewDto.class,
				(conf, source, destination) -> conf
					.beforeMap(mapperRef -> Logger.debug("Starting mapping of Order (id: " + source.getId() + ") by mapper " + mapperRef))
					// Some other stuff here...
					.afterMap(mapperRef -> Logger.debug("Finished mapping of Order (id: " + source.getId() + ") by mapper " + mapperRef))
					).buildMapper();

		Order order = new Order();
		order.setId(22);

		// WHEN
		mapper.map(order, OrderOverviewDto.class);

		// THEN: Life goes on ;-)
	}
}
