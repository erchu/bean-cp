package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects2.Line;
import com.github.erchu.beancp.tutorial.objects2.LineDto;
import com.github.erchu.beancp.tutorial.objects2.Point;
import com.github.erchu.beancp.tutorial.objects2.PointDto;

public class _06_Declarative_maps_in_deep_Map_inner {

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder()
			.addMap(Point.class, PointDto.class, (conf, source, destination) -> conf
					.bind(source::getX, destination::setX)
					.bind(source::getY, destination::setY))
			.addMap(Line.class, LineDto.class, (conf, source, destination) -> conf
					.mapInner(source::getStart, destination::setStart, PointDto.class)
					.mapInner(source::getEnd, destination::setEnd, PointDto.class))
			.buildMapper();
		
		Point start = new Point();
		start.setX(20);
		start.setY(30);
		
		Point end = new Point();
		end.setX(-8);
		end.setY(22);
		
		Line line = new Line();
		line.setStart(start);
		line.setEnd(end);

		// WHEN
		LineDto destination = mapper.map(line, LineDto.class);

		// THEN
		assertEquals(start.getX(), destination.getStart().getX());
		assertEquals(start.getY(), destination.getStart().getY());
		assertEquals(end.getX(), destination.getEnd().getX());
		assertEquals(end.getY(), destination.getEnd().getY());
	}
}
