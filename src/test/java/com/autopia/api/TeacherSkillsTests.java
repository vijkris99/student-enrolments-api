package com.autopia.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Teacher;

import lombok.SneakyThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeacherSkillsTests extends BaseTest {
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToAssociateMultipleSkillsWithSameTeacher() {
		// Given a teacher and 2 skills
		
		// When I associate both the skills with the teacher
		mockMvc
			.perform(post("/teachers/{id}/skills", savedTeacher1.getId())
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/1\n/skills/2"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// Then the teacher should be associated with both the skills
		mockMvc
			.perform(get("/teachers/{id}/skills", savedTeacher1.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[*].name",
						hasItems(savedSkill1.getName(), savedSkill2.getName())));
		
		// And the teacher should be searchable by the first skill
		mockMvc
			.perform(get("/teachers/search/findBySkills")
					.param("skill", "/skills/1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[0]_links.self.href",
											containsString("/teachers/1")));
		
		// And the teacher should be searchable by the second skill
		mockMvc
			.perform(get("/teachers/search/findBySkills")
					.param("skill", "/skills/2"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[0]_links.self.href",
											containsString("/teachers/1")));
		
		// And the teacher should be searchable by both skills together
		mockMvc
			.perform(get("/teachers/search/findBySkillsIn")
					.param("skills", "/skills/1", "/skills/2"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers[0]_links.self.href",
											containsString("/teachers/1")));
	}
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToAssociateSkillWithMultipleTeachers() {
		// Given a skill and 2 teachers
		mockMvc.perform(get("/teachers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded").exists())
				.andExpect(jsonPath("$._links.self").exists())
				.andExpect(jsonPath("$._links.profile").exists())
				.andExpect(jsonPath("$._embedded.teachers").isArray())
				.andExpect(jsonPath("$._embedded.teachers.length()").value(2));
		
		// When I associate the skill with the first teacher
		mockMvc
			.perform(post("/teachers/{id}/skills", savedTeacher1.getId())
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/" + savedSkill1.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// And I associate the skill with the second teacher
		mockMvc
			.perform(post("/teachers/{id}/skills", savedTeacher2.getId())
					.contentType("text/uri-list")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("/skills/" + savedSkill1.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// Then the skill should be associated with the first teacher
		mockMvc
			.perform(get("/teachers/{id}/skills", savedTeacher1.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[0].name", is(savedSkill1.getName())));
		
		// And the skill should be associated with the second teacher
		mockMvc
			.perform(get("/teachers/{id}/skills", savedTeacher2.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[0].name", is(savedSkill1.getName())));
		
		// And both teachers should be searchable by the skill
		mockMvc
			.perform(get("/teachers/search/findBySkills")
					.param("skill", "/skills/" + savedSkill1.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teachers.length()", is(2)))
			.andExpect(jsonPath("$._embedded.teachers[*].firstName",
						hasItems(savedTeacher1.getFirstName(), savedTeacher2.getFirstName())))
			.andExpect(jsonPath("$._embedded.teachers[*].lastName",
						hasItems(savedTeacher1.getLastName(), savedTeacher2.getLastName())));
	}
	
	@Test
	@SneakyThrows
	public void shouldBeAbleToDisassociateSkillFromTeacher() {
		//Given a teacher with one associated skill
		savedTeacher1.addSkill(savedSkill1);
		Teacher savedAgainTeacher1 = teacherRepository.save(savedTeacher1);
		assertThat(savedAgainTeacher1.getSkills(), contains(savedSkill1));
		
		// When I delete the association
		mockMvc
			.perform(delete("/teachers/{teacher_id}/skills/{skill_id}",
								savedAgainTeacher1.getId(), savedSkill1.getId()))
			.andExpect(status().isNoContent());
		
		// Then the teacher should have no associated skill
		Teacher foundTeacher1 = teacherRepository.findOne(savedAgainTeacher1.getId());
		assertThat(foundTeacher1.getSkills().size(), is(0));
		assertThat(foundTeacher1.getSkills(), not(contains(savedSkill1)));
		
		// And the skill should be unaffected by the disassociation
		Skill foundSkill1 = skillRepository.findOne(savedSkill1.getId());
		assertThat(foundSkill1).isNotNull();
		assertThat(foundSkill1.getName()).isEqualTo("Keyboard");
	}
}