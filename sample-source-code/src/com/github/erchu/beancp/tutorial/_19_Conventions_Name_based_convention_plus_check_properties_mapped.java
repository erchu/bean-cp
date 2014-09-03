package com.github.erchu.beancp.tutorial;

import org.junit.Test;

import com.github.erchu.beancp.Mapper;
import com.github.erchu.beancp.MapperBuilder;
import com.github.erchu.beancp.commons.NameBasedMapConvention;
import com.github.erchu.beancp.tutorial.objects4.User;
import com.github.erchu.beancp.tutorial.objects4.UserDto;

public class _19_Conventions_Name_based_convention_plus_check_properties_mapped {
	
	public static class MapperProvider {

		@SuppressWarnings("unchecked")
		public Mapper getMapper() {
			return new MapperBuilder()
			.addMap(User.class, UserDto.class, (conf, source, destination) -> conf
					.useConvention(NameBasedMapConvention.get()
							.excludeDestinationMembers("FullName")
							.failIfNotAllDestinationMembersMapped())
					.bind(() -> source.getFirstName() + ' ' + source.getLastName(), destination::setFullName))
			.buildMapper();
		}
	}
	
	public static class MapperProviderTest {

		@Test
		public void mapper_configuration_is_valid() {
			// This test only check if getMapper() will not throw any exception
			new MapperProvider().getMapper();
		}
	}
}
