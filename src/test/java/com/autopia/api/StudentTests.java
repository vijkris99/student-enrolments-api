package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@SneakyThrows
	@Test
	public void getStudentsShouldSucceed() {
		this.mockMvc.perform(get("/students")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void createStudentShouldSucceed() {
		Student student = new Student();
		student.setFirstName("Pranav");
		
		this.mockMvc
			.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(student)))
			.andDo(print())
			.andExpect(status().isCreated());
	}
}