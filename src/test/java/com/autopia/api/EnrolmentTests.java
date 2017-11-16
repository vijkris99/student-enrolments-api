package com.autopia.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.EnrolmentRepository;
import com.autopia.data.repositories.SkillRepository;
import com.autopia.data.repositories.StudentRepository;
import com.autopia.data.repositories.TeacherRepository;
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
public class EnrolmentTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private SkillRepository skillRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private EnrolmentRepository enrolmentRepository;
	
	private Teacher teacher1 = new Teacher();
	private Teacher teacher2 = new Teacher();
	private Skill skill1 = new Skill();
	private Skill skill2 = new Skill();
	private Student student1 = new Student();
	private Student student2 = new Student();
	
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
		
		student1.setFirstName("Pranav");
		student2.setFirstName("Arwin");
		
		if(!dataInitialized) {
			teacherRepository.deleteAll();
			skillRepository.deleteAll();
			studentRepository.deleteAll();
			
			teacherRepository.save(teacher1);
			teacherRepository.save(teacher2);
			skillRepository.save(skill1);
			skillRepository.save(skill2);
			studentRepository.save(student1);
			studentRepository.save(student2);
			
			dataInitialized = true;
		}
	}
	
	@SneakyThrows
	@Test
	public void getEnrolmentsShouldSucceed() {
		mockMvc.perform(get("/enrolments")).andExpect(status().isOk());
	}
	
	@SneakyThrows
	@Test
	public void getEnrolmentShouldSucceed() {
		//Given a teacher, skill and student
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		
		enrolmentJson.put("teacher", "/teachers/1");
		enrolmentJson.put("student", "/students/1");
		enrolmentJson.put("skill", "/skills/1");
		enrolmentJson.put("sessionFee", 20);
		enrolmentJson.put("isActive", true);
		//enrolmentJson.put("startDate", LocalDate.now().toString());
		
		// When I enroll the student with the teacher to learn the skill
		mockMvc
			.perform(post("/enrolments")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(enrolmentJson.toString()))
			.andDo(print())
			.andExpect(status().isCreated());
		
		mockMvc
			.perform(get("/enrolments/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.teacher.firstName").value(teacher1.getFirstName()));
	}
	
	@SneakyThrows
	@Test
	public void enrolmentAssociationsShouldBeBidirectional() {
		//Given a teacher, skill and student
		ObjectNode enrolmentJson = objectMapper.createObjectNode();
		
		enrolmentJson.put("teacher", "/teachers/1");
		enrolmentJson.put("student", "/students/1");
		enrolmentJson.put("skill", "/skills/1");
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
			.perform(get("/enrolments/{id}/skill", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillName", is(skill1.getSkillName())));
		
		// And the skill should be associated with the enrolment
		mockMvc
			.perform(get("/skills/{id}/enrolments", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/1")));
		
		// And the enrolment should be associated with the teacher
		mockMvc
			.perform(get("/enrolments/{id}/teacher", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(teacher1.getFirstName())));
		
		// And the teacher should be associated with the enrolment
		mockMvc
			.perform(get("/teachers/{id}/enrolments", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/1")));
		
		// And the enrolment should be associated with the student
		mockMvc
			.perform(get("/enrolments/{id}/student", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(student1.getFirstName())));
		
		// And the student should be associated with the enrolment
		mockMvc
			.perform(get("/students/{id}/enrolments", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.enrolments[0]_links.self.href",
											containsString("/enrolments/1")));
	}
	
	@After
	public void tearDown() {
		enrolmentRepository.deleteAll();
	}
}