package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.autopia.data.entities.Enrolment;
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
	
	@SneakyThrows
	@Test
	public void getEnrolmentsShouldSucceed() {
		mockMvc.perform(get("/enrolments"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists())
						.andExpect(jsonPath("$._embedded.enrolments").isArray())
						.andExpect(jsonPath("$._embedded.enrolments.length()").value(1));
	}
	
	@SneakyThrows
	@Test
	public void getEnrolmentShouldReturnCorrectData() {
		// Given an enrolment
		
		// When I read the enrolment
		// Then I should succeed
		// And the enrolment should contain links to the corresponding teacher, skill and student
		mockMvc
			.perform(get("/enrolments/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.teacher.href").exists())
			.andExpect(jsonPath("$._links.skill.href").exists())
			.andExpect(jsonPath("$._links.student.href").exists());
	}
	
	@SneakyThrows
	@Test
	public void enrolmentAssociationsShouldBeBidirectional() {
		//Given a teacher, skill and student
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		
		enrolmentJson.put("teacher", "/teachers/2");
		enrolmentJson.put("skill", "/skills/2");
		enrolmentJson.put("student", "/students/2");
		enrolmentJson.put("sessionFee", 20);
		//enrolmentJson.put("startDate", LocalDate.now().toString());
		
		log.info(enrolmentJson.toString());
		log.info(objectMapper.writeValueAsString(enrolmentJson));
		
		// When I enroll the student with the teacher to learn the skill
		MvcResult result = mockMvc
							.perform(post("/enrolments")
									.contentType(MediaType.APPLICATION_JSON_UTF8)
									.accept(MediaType.APPLICATION_JSON_UTF8)
									.content(enrolmentJson.toString()))
							.andDo(print())
							.andExpect(status().isCreated())
							.andExpect(jsonPath("$.sessionFee", is(20)))
							.andExpect(jsonPath("$.balanceDue", is(0)))
							.andExpect(jsonPath("$.isActive", is(true)))
							.andReturn();
		
		String createdEnrolmentLocation = result.getResponse().getHeader("Location");
		long createdEnrolmentId = Long.parseLong(
								createdEnrolmentLocation.replace("http://localhost/enrolments/", ""));
		
		// Then the enrolment should be associated with the skill
		mockMvc
			.perform(get("/enrolments/{id}/skill", createdEnrolmentId))
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
											containsString("/enrolments/" + createdEnrolmentId)));
		
		// And the enrolment should be associated with the teacher
		mockMvc
			.perform(get("/enrolments/{id}/teacher", createdEnrolmentId))
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
											containsString("/enrolments/" + createdEnrolmentId)));
		
		// And the enrolment should be associated with the student
		mockMvc
			.perform(get("/enrolments/{id}/student", createdEnrolmentId))
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
											containsString("/enrolments/" + createdEnrolmentId)));
		
		// Clean up
		enrolmentRepository.delete(createdEnrolmentId);
	}
	
	@SneakyThrows
	@Test
	public void replaceEnrolmentShouldSucceed() {
		// Given an existing enrolment
		Enrolment enrolment = new Enrolment();
		enrolment.setTeacher(teacher2);
		enrolment.setSkill(skill2);
		enrolment.setStudent(student2);
		enrolment.setSessionFee(20);
		Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
		
		// When I replace the existing entry for the enrolment
		// Then it should succeed
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		enrolmentJson.put("teacher", "/teachers/2");
		enrolmentJson.put("skill", "/skills/2");
		enrolmentJson.put("student", "/students/2");
		enrolmentJson.put("sessionFee", 25);
		
		mockMvc
			.perform(put("/enrolments/{id}", savedEnrolment.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(enrolmentJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sessionFee", is(25)))
			.andExpect(jsonPath("$.balanceDue", is(0)))
			.andExpect(jsonPath("$.isActive", is(true)));
		
		// Clean up
		enrolmentRepository.delete(savedEnrolment.getId());
	}
	
	@SneakyThrows
	@Test
	public void updateEnrolmentShouldSucceed() {
		// Given an existing enrolment
		Enrolment enrolment = new Enrolment();
		enrolment.setTeacher(teacher2);
		enrolment.setSkill(skill2);
		enrolment.setStudent(student2);
		enrolment.setSessionFee(20);
		Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
		
		// When I update the existing entry for the enrolment
		// Then it should succeed
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		enrolmentJson.put("sessionFee", 25);
		
		mockMvc
			.perform(patch("/enrolments/{id}", savedEnrolment.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(enrolmentJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sessionFee", is(25)))
			.andExpect(jsonPath("$.balanceDue", is(0)))
			.andExpect(jsonPath("$.isActive", is(true)));
		
		// Clean up
		enrolmentRepository.delete(savedEnrolment.getId());
	}
	
	@SneakyThrows
	@Test
	public void deleteEnrolmentShouldSucceed() {
		// Given an existing enrolment
		Enrolment enrolment = new Enrolment();
		enrolment.setTeacher(teacher2);
		enrolment.setSkill(skill2);
		enrolment.setStudent(student2);
		enrolment.setSessionFee(20);
		Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
		
		// When I delete the enrolment
		// Then it should succeed
		mockMvc.perform(delete("/enrolments/{id}", savedEnrolment.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// TODO: Search and make sure the enrolment got deleted
	}
}