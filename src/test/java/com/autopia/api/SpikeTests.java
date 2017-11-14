package com.autopia.api;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autopia.data.entities.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpikeTests {

	@Autowired
	private ObjectMapper objectMapper;
	
	@SneakyThrows
	@Test
	public void testObjectMapper() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9732621588");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		log.info("Hey!" + objectMapper.writeValueAsString(teacher));
	}
}
