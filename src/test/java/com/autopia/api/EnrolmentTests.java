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
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class EnrolmentTests extends BaseTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@SneakyThrows
	@Test
	public void getEnrolmentsShouldSucceed() {
		mockMvc.perform(get("/enrolments")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void getEnrolmentShouldSucceed() {
		// Given an enrolment
		
		// When I read the enrolment
		// Then I should succeed
		// And the enrolment should contain links to the corresponding teacher, skill and student
		mockMvc
			.perform(get("/enrolments/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.teacher").exists())
			.andExpect(jsonPath("$._links.skill").exists())
			.andExpect(jsonPath("$._links.student").exists());
	}
	
	@SneakyThrows
	@Test
	public void enrolmentAssociationsShouldBeBidirectional() {
		//Given a teacher, skill and student
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		
		enrolmentJson.put("teacher", "/teachers/2");
		enrolmentJson.put("student", "/students/2");
		enrolmentJson.put("skill", "/skills/2");
		enrolmentJson.put("sessionFee", 20);
		enrolmentJson.put("isActive", true);
		//enrolmentJson.put("startDate", LocalDate.now().toString());
		
		log.info(enrolmentJson.toString());
		log.info(objectMapper.writeValueAsString(enrolmentJson));
		
		// When I enroll the student with the teacher to learn the skill
		mockMvc
			.perform(post("/enrolments")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(enrolmentJson.toString()))
			.andDo(print())
			.andExpect(status().isCreated());
		
		// Then the enrolment should be associated with the skill
		mockMvc
			.perform(get("/enrolments/{id}/skill", 2))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillName", is(skill2.getSkillName())));
		
		// And the skill should be associated with the enrolment
		mockMvc
			.perform(get("/enrolments/search/findBySkill")
					.param("skill", "/skills/2"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/2")));
		
		// And the enrolment should be associated with the teacher
		mockMvc
			.perform(get("/enrolments/{id}/teacher", 2))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(teacher2.getFirstName())));
		
		// And the teacher should be associated with the enrolment
		mockMvc
			.perform(get("/enrolments/search/findByTeacher")
					.param("teacher", "/teachers/2"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/2")));
		
		// And the enrolment should be associated with the student
		mockMvc
			.perform(get("/enrolments/{id}/student", 2))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(student2.getFirstName())));
		
		// And the student should be associated with the enrolment
		mockMvc
			.perform(get("/enrolments/search/findByStudent")
					.param("student", "students/2"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/2")));
	}
}