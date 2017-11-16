package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SkillTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@SneakyThrows
	@Test
	public void getSkillssShouldSucceed() {
		mockMvc.perform(get("/skills")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void createSkillShouldSucceed() {
		String skillJson = "{\"skillName\":\"Keyboard\"}";
		
		mockMvc
			.perform(post("/skills")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(skillJson))
			.andDo(print())
			.andExpect(status().isCreated());
	}
}