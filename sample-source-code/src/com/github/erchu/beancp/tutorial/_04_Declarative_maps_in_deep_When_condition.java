package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.erchu.beancp.BindingOption;
import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects1.Customer;
import com.github.erchu.beancp.tutorial.objects1.Order;
import com.github.erchu.beancp.tutorial.objects1.OrderOverviewDto;

public class _04_Declarative_maps_in_deep_When_condition {

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Order.class,
				OrderOverviewDto.class,
				(conf, source, destination) -> conf.bind(
						source::getTotalAmount,
						destination::setTotalAmount,
						BindingOption.mapWhen(() -> source.getTotalAmount().compareTo(BigDecimal.ZERO) > 0)))
				.buildMapper();

		Customer customer = new Customer();
		customer.setId(1);
		customer.setFullName("Sample Customer");

		Order order = new Order();
		order.setId(2);
		order.setTotalAmount(BigDecimal.valueOf(-2));
		order.setCustomer(customer);

		// WHEN
		OrderOverviewDto destination = mapper.map(order, OrderOverviewDto.class);

		// THEN
		assertNull(destination.getTotalAmount());
	}
}
