package com.github.erchu.beancp.tutorial;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.NameBasedMapConvention;
import com.github.erchu.beancp.tutorial.objects4.User;
import com.github.erchu.beancp.tutorial.objects4.UserDto;

public class _20_Conventions_Map_any {

	@Test
	public void test() {
		// GIVEN
		Mapper mapper = new MapperBuilder()
			.addMapAnyByConvention(NameBasedMapConvention.get())
			.buildMapper();
		
		User user = new User();
		user.setId(100);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setPassword("password1");
		user.setPhoneNumber("+48 12 300 20 10");
		user.setEmailAddress("e@e.com");

		// WHEN
		UserDto result = new UserDto();
		mapper.map(user, result);

		// THEN
		assertEquals(user.getId(), result.getId());
		assertEquals(user.getFirstName(), result.getFirstName());
		assertEquals(user.getLastName(), result.getLastName());
		assertEquals(user.getPassword(), result.getPassword());
		assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(user.getEmailAddress(), result.getEmailAddress());
		assertEquals("firstName1 lastName1", result.getFullName());
	}
}
