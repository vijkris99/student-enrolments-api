package com.autopia.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.autopia.data.entities.Student;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentTests extends BaseTest {
	
	@SneakyThrows
	@Test
	public void getStudentsShouldSucceed() {
		mockMvc.perform(get("/students"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists())
						.andExpect(jsonPath("$._embedded.students").isArray())
						.andExpect(jsonPath("$._embedded.students.length()").value(2));
	}
	
	@SneakyThrows
	@Test
	public void getStudentShouldReturnCorrectData() {
		// Given an existing student
		
		// When I read the student
		// Then I should succeed
		MvcResult mvcResult = mockMvc.perform(get("/students/{id}", 1))
										.andDo(print())
										//.andExpect(status().isOk())
										.andExpect(jsonPath("$.firstName").value("Pranav"))
										.andReturn();
		
		// And the student details should be accurate
		Assert.assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@SneakyThrows
	@Test
	public void createStudentShouldSucceed() {
		// Given a new student
		String studentJson = "{\"firstName\":\"Mythri\"}";
		
		// When I create a new entry for the student
		// Then it should succeed
		MvcResult result = mockMvc
							.perform(post("/students")
									.contentType(MediaType.APPLICATION_JSON_UTF8)
									.accept(MediaType.APPLICATION_JSON_UTF8)
									.content(studentJson))
							.andDo(print())
							.andExpect(status().isCreated())
							.andExpect(jsonPath("$.firstName").value("Mythri"))
							.andReturn();
		
		// Clean up
		String createdStudentLocation = result.getResponse().getHeader("Location");
		long createdStudentId = Long.parseLong(
								createdStudentLocation.replace("http://localhost/students/", ""));
		studentRepository.delete(createdStudentId);
	}
	
	@SneakyThrows
	@Test
	public void replaceStudentShouldSucceed() {
		// Given an existing student
		Student student = new Student();
		student.setFirstName("Mythri");
		student.setLastName("Arjun");
		Student savedStudent = studentRepository.save(student);
		
		// When I replace the existing entry for the student
		// Then it should succeed
		ObjectNode studentJson = objectMapper.createObjectNode();
		studentJson.put("firstName", "Mythiri");
		studentJson.put("lastName", "Arjun");
		
		mockMvc
			.perform(put("/students/{id}", savedStudent.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(studentJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Mythiri"))
			.andExpect(jsonPath("$.lastName").value("Arjun"));
		
		// Clean up
		studentRepository.delete(savedStudent.getId());
	}
	
	@SneakyThrows
	@Test
	public void updateStudentShouldSucceed() {
		// Given an existing student
		Student student = new Student();
		student.setFirstName("Mythri");
		student.setLastName("Arjun");
		Student savedStudent = studentRepository.save(student);
		
		// When I update the existing entry for the student
		// Then it should succeed
		ObjectNode studentJson = objectMapper.createObjectNode();
		studentJson.put("firstName", "Mythiri");
		
		mockMvc
			.perform(patch("/students/{id}", savedStudent.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(studentJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Mythiri"))
			.andExpect(jsonPath("$.lastName").value("Arjun"));
		
		// Clean up
		studentRepository.delete(savedStudent.getId());
	}
	
	@SneakyThrows
	@Test
	public void deleteStudentShouldSucceed() {
		// Given an existing student
		Student student = new Student();
		student.setFirstName("Mythri");
		student.setLastName("Arjun");
		Student savedStudent = studentRepository.save(student);
		
		// When I delete the student
		// Then it should succeed
		mockMvc.perform(delete("/students/{id}", savedStudent.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// TODO: Search and make sure the student got deleted
	}
}