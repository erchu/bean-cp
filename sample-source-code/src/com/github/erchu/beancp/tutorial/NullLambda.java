package com.github.erchu.beancp.tutorial;

import java.util.function.Function;

import org.junit.Test;

public class NullLambda {

	public void runIt(Function<Integer, Integer> toCall) {
		Integer x = null;
		
		toCall.apply(x);
	}
	
	@Test
	public void test() {
		runIt(x -> x * 2);
	}
}
