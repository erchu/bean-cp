package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects1.Customer;
import com.github.erchu.beancp.tutorial.objects1.Order;
import com.github.erchu.beancp.tutorial.objects1.OrderOverviewDto;

public class _01_First_mapping {

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Order.class,
				OrderOverviewDto.class,
				(conf, source, destination) -> conf
						.bind(source::getId, destination::setId)
						.bind(source.getCustomer()::getFullName,
								destination::setCustomerFullName)
						.bind(source::getTotalAmount,
								destination::setTotalAmount)).buildMapper();
		
		Customer customer = new Customer();
		customer.setId(1);
		customer.setFullName("Sample Customer");
		
		Order order = new Order();
		order.setId(2);
		order.setTotalAmount(BigDecimal.valueOf(120));
		order.setCustomer(customer);
		
		// WHEN
		OrderOverviewDto destination = mapper.map(order, OrderOverviewDto.class);
		
		// THEN
		assertEquals(customer.getFullName(), destination.getCustomerFullName());
		assertEquals(destination.getId(), order.getId());
		assertEquals(destination.getTotalAmount(), order.getTotalAmount());
	}
}
