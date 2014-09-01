package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.erchu.beancp.BindingOption;
import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;

public class _05_Declarative_maps_in_deep_null_substitution {

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Order.class,
				OrderOverviewDto.class,
				(conf, source, destination) -> conf.bind(
						source.getCustomer()::getFullName,
						destination::setCustomerFullName,
						BindingOption.withNullSubstitution("unknown")))
				.buildMapper();

		Customer customer = new Customer();
		customer.setId(1);
		customer.setFullName(null);

		Order order = new Order();
		order.setId(2);
		order.setTotalAmount(BigDecimal.valueOf(-2));
		order.setCustomer(customer);

		// WHEN
		OrderOverviewDto destination = mapper
				.map(order, OrderOverviewDto.class);

		// THEN
		assertEquals("unknown", destination.getCustomerFullName());
	}
}
