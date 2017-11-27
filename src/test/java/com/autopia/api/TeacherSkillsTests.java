package com.autopia.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import lombok.SneakyThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherSkillsTests extends BaseTest {
	
	@Test
	@SneakyThrows
	public void teacherSkillAssociationShouldBeBidirectional() {
		// Given a teacher and a skill
		// When I associate the skill with the teacher
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// Then the teacher should be associated with the skill
		mockMvc
			.perform(get("/teachers/{id}/skills", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[:1].skillName").value(skill1.getSkillName()));
		
		// And the skill should be associated with the teacher
		mockMvc
			.perform(get("/skills/{id}/teachers", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[0].firstName").value(teacher1.getFirstName()))
			.andExpect(jsonPath("$._embedded.teachers[0].lastName").value(teacher1.getLastName()))
			.andExpect(jsonPath("$._embedded.teachers[0].phoneNumber").value(teacher1.getPhoneNumber()));
	}
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToAssociateMultipleSkillsWithSameTeacher() {
		// Given a teacher and 2 skills
		
		// When I associate both the skills with the teacher
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1\n/skills/2"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// Then the teacher should be associated with both the skills
		mockMvc
			.perform(get("/teachers/{id}/skills", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[*].skillName",
						hasItems(skill1.getSkillName(),skill2.getSkillName())));
	}
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToAssociateSkillWithMultipleTeachers() {
		// Given a skill and 2 teachers
		
		// When I associate the skill with both teachers
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		mockMvc
			.perform(post("/teachers/{id}/skills", 2)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// Then the skill should be associated with both the teachers
		mockMvc
			.perform(get("/skills/{id}/teachers", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[*].firstName",
								hasItems(teacher1.getFirstName(), teacher2.getFirstName())));
	}
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToDisassociateSkillFromTeacher() {
		//Given a teacher with one associated skill
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// When I delete the association
		mockMvc
			.perform(delete("/teachers/{teacher_id}/skills/{skill_id}", 1, 1))
			.andExpect(status().isNoContent());
		
		// Then the teacher should have no associated skill
		mockMvc
			.perform(get("/teachers/{id}/skills", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills").isEmpty());
	}
	
	@After
	public void tearDown() {
		teacher1 = teacherRepository.findOne((long) 1);
		teacher1.setSkills(null);	// Clear skills associated with the test teacher1
		teacherRepository.save(teacher1);
		
		teacher2 = teacherRepository.findOne((long) 2);
		teacher2.setSkills(null);	// Clear skills associated with the test teacher2
		teacherRepository.save(teacher2);
	}
}