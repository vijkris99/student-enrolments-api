package com.autopia.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.SkillRepository;
import com.autopia.data.repositories.TeacherRepository;

import lombok.SneakyThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherSkillsTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private SkillRepository skillRepository;
	
	private Teacher teacher1 = new Teacher();
	private Teacher teacher2 = new Teacher();
	private Skill skill1 = new Skill();
	private Skill skill2 = new Skill();
	
	private static Boolean dataInitialized = false;
	
	@Before
	public void setup() {
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9732621588");
		
		teacher2.setFirstName("Madhav");
		teacher2.setLastName("Bhatki");
		teacher2.setPhoneNumber("1234567890");
		
		skill1.setSkillName("Keyboard");
		skill2.setSkillName("Guitar");
		
		if(!dataInitialized) {
			teacherRepository.deleteAll();
			skillRepository.deleteAll();
			
			teacherRepository.save(teacher1);
			teacherRepository.save(teacher2);
			skillRepository.save(skill1);
			skillRepository.save(skill2);
			
			dataInitialized = true;
		}
	}
	
	@Test
	@SneakyThrows
	public void shouldNotBeAbleToAssociateSameSkillTwiceWithSameTeacher() {
		// Given a teacher with an associated skill
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// When I try to associate the same skill again with the teacher
		// Then I should get a conflict error
		mockMvc
			.perform(post("/teachers/{id}/skills", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1"))
			.andDo(print())
			.andExpect(status().isConflict());
	}
	
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
			.andExpect(jsonPath("$._embedded.skills[0].skillName").value(skill1.getSkillName()))
			.andExpect(jsonPath("$._embedded.skills[1].skillName").value(skill2.getSkillName()));
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
			.andExpect(jsonPath("$._embedded.teachers[0].firstName").value(teacher1.getFirstName()))
			.andExpect(jsonPath("$._embedded.teachers[1].firstName").value(teacher2.getFirstName()));
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
	
	@Ignore
	@Test
	@SneakyThrows
	public void skillTeacherAssociationShouldBeBidirectional() {
		mockMvc
			.perform(post("/skills/{id}/teachers", 1)
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/teachers/1"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		mockMvc
			.perform(get("/teachers/{id}/skills", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[:1].skillName").value(skill1.getSkillName()));
		
		mockMvc
			.perform(get("/skills/{id}/teachers", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[:1].firstName").value(teacher1.getFirstName()))
			.andExpect(jsonPath("$._embedded.teachers[:1].lastName").value(teacher1.getLastName()))
			.andExpect(jsonPath("$._embedded.teachers[:1].phoneNumber").value(teacher1.getPhoneNumber()));
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