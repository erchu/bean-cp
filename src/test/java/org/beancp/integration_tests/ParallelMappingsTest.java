/*
 * bean-cp
 * Copyright (c) 2014, Rafal Chojnacki, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package org.beancp.integration_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.Thread.UncaughtExceptionHandler;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.beancp.Mapper;
import org.beancp.MapperBuilder;
import org.beancp.commons.NameBasedMapConvention;
import org.junit.Test;

// Warning: this test is ugly and proves almost nothing, but still is better to have it ;-)
public class ParallelMappingsTest {
	
	private static final int NUMBER_OF_THREADS = 10;
	
	private static final int TEST_DURATION_SECONDS = 10;

	public static class SourceInner {

		private int z;

		public int getZ() {
			return z;
		}

		public void setZ(int z) {
			this.z = z;
		}
	}

	public static class Source {

		private int x;

		public int y;

		private SourceInner inner;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public SourceInner getInner() {
			return inner;
		}

		public void setInner(SourceInner inner) {
			this.inner = inner;
		}
	}

	public static class Destination {

		private int xPlusY;

		private int innerZ;

		public int getXPlusY() {
			return xPlusY;
		}

		public void setXPlusY(int xPlusY) {
			this.xPlusY = xPlusY;
		}

		public int getInnerZ() {
			return innerZ;
		}

		public void setInnerZ(int innerZ) {
			this.innerZ = innerZ;
		}
	}

	private class MappingThread implements Runnable {

		private Mapper mapper;
		
		private Random random;

		public MappingThread(final Mapper mapper) {
			this.mapper = mapper;
			this.random = new Random();
		}

		@Override
		public void run() {
			LocalDateTime start = LocalDateTime.now();
			
			do {
				executeMapping();
			} while (testTimeElapsed(start) == false);
		}

		private boolean testTimeElapsed(final LocalDateTime start) {
			Duration testDuration = Duration.between(start, LocalDateTime.now());
			
			return (testDuration.getSeconds() > TEST_DURATION_SECONDS);
		}

		private void executeMapping() {
			SourceInner inner = new SourceInner();
			inner.setZ(random.nextInt());
			
			Source source = new Source();
			source.setX(random.nextInt());
			source.setY(random.nextInt());
			source.setInner(inner);
			
			Destination result = mapper.map(source, Destination.class);
			
			assertEquals(source.getX() + source.getY(), result.getXPlusY());
			assertEquals(source.getInner().getZ(), result.getInnerZ());
		}
	}
	
	private static class VolatileBooleanHolder {
		
		private volatile boolean value;

		public VolatileBooleanHolder(final boolean value) {
			this.value = value;
		}
		
		public boolean getValue() {
			return value;
		}

		public void setValue(boolean value) {
			this.value = value;
		}
	}

	@Test
	public void mapper_should_be_able_to_map_objects_in_parallel_threads()
			throws InterruptedException {
		// GIVEN
		Mapper mapper = new MapperBuilder().addMap(
				Source.class,
				Destination.class,
				(config, source, destination) -> config.useConvention(
						NameBasedMapConvention.get().enableFlattening()).bind(
						() -> source.getX() + source.getY(),
						destination::setXPlusY)).buildMapper();

		// WHEN
		Thread[] threads = new Thread[NUMBER_OF_THREADS];
		VolatileBooleanHolder success = new VolatileBooleanHolder(true);

		for (int i = 0; i < threads.length; i++) {
			Thread iThread = new Thread(new MappingThread(mapper), "Test #" + i);
			
			iThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					success.setValue(false);
					
					System.err.println(e);
				}
			});
			
			threads[i] = iThread;

			iThread.start();
		}

		for (Thread i : threads) {
			i.join();
		}

		// THEN

		assertTrue("Something went wrong...", success.getValue());
	}
}
