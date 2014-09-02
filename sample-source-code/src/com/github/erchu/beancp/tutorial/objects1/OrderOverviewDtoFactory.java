package com.github.erchu.beancp.tutorial.objects1;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderOverviewDtoFactory {
	
	private static AtomicInteger idGenerator = new AtomicInteger(100);

	public static OrderOverviewDto getOrderOverviewDto() {
		OrderOverviewDto result = new OrderOverviewDto();
		result.setId(idGenerator.getAndIncrement());
		
		return result;
	}
}
