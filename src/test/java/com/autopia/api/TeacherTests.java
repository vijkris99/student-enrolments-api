package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherTests extends BaseTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@SneakyThrows
	@Test
	public void getTeachersShouldSucceed() {
		mockMvc.perform(get("/teachers"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists());
	}
	
	@SneakyThrows
	@Test
	public void getTeacherShouldReturnCorrectData() {
		mockMvc.perform(get("/teachers/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Vijay"));
	}
	
	@SneakyThrows
	@Test
	public void createTeacherShouldSucceed() {
		ObjectNode teacherJson = objectMapper.createObjectNode();
		teacherJson.put("firstName", "Sangeetha");
		teacherJson.put("lastName", "Chandrashekhar");
		teacherJson.put("phoneNumber", "9732621578");
		
		mockMvc
			.perform(post("/teachers")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(teacherJson.toString()))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Sangeetha"));
	}
}