package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class EnrolmentTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@SneakyThrows
	@Test
	public void getEnrolmentsShouldSucceed() {
		this.mockMvc.perform(get("/enrolments")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void createEnrolmentShouldSucceed() {
		createTeacher();
		createSkill();
		associateSkillWithTeacher();
		createStudent();
		
		Enrolment enrolment = new Enrolment();
		//enrolment.setTeacher(teacher);
		//enrolment.setStudent(student);
		//enrolment.setSkill(skill);
		enrolment.setSessionFee(20);
		enrolment.setIsActive(true);
		enrolment.setStartDate(LocalDate.now());
		
		//String enrolmentJson = objectMapper.writeValueAsString(enrolment);
		//ObjectNode objectNode = (ObjectNode) objectMapper.readTree(enrolmentJson);
		
		//JsonNode jsonNode = objectMapper.valueToTree(enrolment);
		JsonNode jsonNode = objectMapper.convertValue(enrolment, JsonNode.class);
		log.info("Hey!", jsonNode.asText());
		
		//ObjectNode objectNode = (ObjectNode) objectMapper.valueToTree(enrolment);
		ObjectNode objectNode = objectMapper.convertValue(enrolment, ObjectNode.class);
		log.info("Hey!" + objectNode.asText());
		
		objectNode.put("teacher", "/teachers/1");
		objectNode.put("student", "/students/1");
		objectNode.put("skill", "/skills/1");
		log.info("Hey!" + objectNode.asText());
		
		this.mockMvc
				.perform(post("/enrolments").contentType(MediaType.APPLICATION_JSON)
				.content(objectNode.asText()))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@SneakyThrows
	private void createTeacher() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9732621588");
		
		this.mockMvc
			.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(teacher)))
			.andDo(print())
			.andExpect(status().isCreated());
	}
	
	@SneakyThrows
	private void createSkill() {
		Skill skill = new Skill();
		skill.setSkillName("Keyboard");
		
		this.mockMvc
			.perform(post("/skills").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(skill)))
			.andDo(print())
			.andExpect(status().isCreated());
	}
	
	@SneakyThrows
	private void associateSkillWithTeacher() {
		this.mockMvc
		.perform(post("/teachers/{id}/skills", 1).contentType("text/uri-list")
				.content("/skills/1"))
		.andDo(print())
		.andExpect(status().isNoContent());
	}
	
	@SneakyThrows
	private void createStudent() {
		Student student = new Student();
		student.setFirstName("Pranav");
		
		this.mockMvc
			.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(student)))
			.andDo(print())
			.andExpect(status().isCreated());
	}
}