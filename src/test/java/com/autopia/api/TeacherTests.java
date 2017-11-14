package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TeacherTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@SneakyThrows
	@Test
	public void getTeachersShouldSucceed() {
		mockMvc.perform(get("/teachers")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void createTeacherShouldSucceed() {
		createTeacher();
	}
	
	@SneakyThrows
	private void createTeacher() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9732621588");
		
		//ObjectMapper objectMapper = new ObjectMapper();
		//log.info(objectMapper.writeValueAsString(teacher));
		
		String teacherJson = "{\"firstName\":\"Vijay\",\"lastName\":\"Ramaswamy\",\"phoneNumber\":\"9732621588\"}";
		
		
		mockMvc
			.perform(post("/teachers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(teacherJson))
			//.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Vijay"));
	}
	
	@Test
	@SneakyThrows
	public void associateSkillWithTeacherShouldSucceed() {
		createTeacher();
		Skill skill = createSkill();
		
		mockMvc
			.perform(post("/teachers/{id}/skills", 1).contentType("text/uri-list")
			.content("/skills/10"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		/*mockMvc
			.perform(post("/teachers/{id}/skills", 1).contentType("text/uri-list")
			.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isConflict());*/
		
		mockMvc
			.perform(get("/teachers/{id}/skills", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[:1]").value(skill));
		
		/*mockMvc
				.perform(get("/skills/{id}/teachers", 1))
				.andDo(print())
				.andExpect(status().isOk());*/
	}
	
	@SneakyThrows
	private Skill createSkill() {
		Skill skill = new Skill();
		skill.setSkillName("Keyboard");
		
		mockMvc
			.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(skill)))
			.andDo(print())
			.andExpect(status().isCreated());
		
		return skill;
	}
}