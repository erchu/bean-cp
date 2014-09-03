package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.tutorial.objects4.User;
import com.github.erchu.beancp.tutorial.objects4.UserDto;

public class _16_Conventions_Introduction {

	@Test
	@SuppressWarnings("unchecked")
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder()
			.addMap(User.class, UserDto.class, (conf, source, destination) -> conf
					.bind(source::getId, destination::setId)
		            .bind(source::getFirstName, destination::setFirstName)
		            .bind(source::getLastName, destination::setLastName)
		            .bind(source::getPassword, destination::setPassword)
		            .bind(source::getPhoneNumber, destination::setPhoneNumber)
		            .bind(source::getEmailAddress, destination::setEmailAddress))
			.buildMapper();
		
		User user = new User();
		user.setId(100);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setPassword("password1");
		user.setPhoneNumber("+48 12 300 20 10");
		user.setEmailAddress("e@e.com");

		// WHEN
		UserDto result = mapper.map(user, UserDto.class);

		// THEN
		assertEquals(user.getId(), result.getId());
		assertEquals(user.getFirstName(), result.getFirstName());
		assertEquals(user.getLastName(), result.getLastName());
		assertEquals(user.getPassword(), result.getPassword());
		assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(user.getEmailAddress(), result.getEmailAddress());
	}
}
