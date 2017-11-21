package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.autopia.data.entities.Teacher;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherTests extends BaseTest {
	
	@SneakyThrows
	@Test
	public void getTeachersShouldSucceed() {
		mockMvc.perform(get("/teachers"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists())
						.andExpect(jsonPath("$._embedded.teachers").isArray())
						.andExpect(jsonPath("$._embedded.teachers.length()").value(2));
	}
	
	@SneakyThrows
	@Test
	public void getTeacherShouldReturnCorrectData() {
		// Given an existing teacher
		
		// When I read the teacher
		// Then I should succeed
		// And the teacher details should be accurate
		mockMvc.perform(get("/teachers/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Vijay"))
			.andExpect(jsonPath("$.lastName").value("Ramaswamy"))
			.andExpect(jsonPath("$.phoneNumber").value("9732621588"));
	}
	
	@SneakyThrows
	@Test
	public void createTeacherShouldSucceed() {
		// Given a new teacher
		ObjectNode teacherJson = objectMapper.createObjectNode();
		teacherJson.put("firstName", "Sangeetha");
		teacherJson.put("lastName", "Chandrashekhar");
		teacherJson.put("phoneNumber", "9732621578");
		
		// When I create a new entry for the teacher
		// Then it should succeed
		MvcResult result = mockMvc
							.perform(post("/teachers")
									.contentType(MediaType.APPLICATION_JSON_UTF8)
									.accept(MediaType.APPLICATION_JSON_UTF8)
									.content(teacherJson.toString()))
							.andDo(print())
							.andExpect(status().isCreated())
							.andExpect(jsonPath("$.firstName").value("Sangeetha"))
							.andExpect(jsonPath("$.lastName").value("Chandrashekhar"))
							.andExpect(jsonPath("$.phoneNumber").value("9732621578"))
							.andReturn();
		
		// Clean up
		String createdTeacherLocation = result.getResponse().getHeader("Location");
		long createdTeacherId = Long.parseLong(
								createdTeacherLocation.replace("http://localhost/teachers/", ""));
		teacherRepository.delete(createdTeacherId);
	}
	
	@SneakyThrows
	@Test
	public void replaceTeacherShouldSucceed() {
		// Given an existing teacher
		Teacher teacher = new Teacher();
		teacher.setFirstName("Sangeetha");
		teacher.setLastName("Chandrashekhar");
		teacher.setPhoneNumber("9732621578");
		Teacher savedTeacher = teacherRepository.save(teacher);
		
		// When I replace the existing entry for the teacher
		// Then it should succeed
		ObjectNode teacherJson = objectMapper.createObjectNode();
		teacherJson.put("firstName", "Sangeetha");
		teacherJson.put("lastName", "VijayKrishnan");
		teacherJson.put("phoneNumber", "9876543210");
		
		mockMvc
			.perform(put("/teachers/{id}", savedTeacher.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(teacherJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Sangeetha"))
			.andExpect(jsonPath("$.lastName").value("VijayKrishnan"))
			.andExpect(jsonPath("$.phoneNumber").value("9876543210"));
		
		// Clean up
		teacherRepository.delete(savedTeacher.getId());
	}
	
	@SneakyThrows
	@Test
	public void updateTeacherShouldSucceed() {
		// Given an existing teacher
		Teacher teacher = new Teacher();
		teacher.setFirstName("Sangeetha");
		teacher.setLastName("Chandrashekhar");
		teacher.setPhoneNumber("9732621578");
		Teacher savedTeacher = teacherRepository.save(teacher);
		
		// When I update the existing entry for the teacher
		// Then it should succeed
		ObjectNode teacherJson = objectMapper.createObjectNode();
		teacherJson.put("lastName", "VijayKrishnan");
		
		mockMvc
			.perform(patch("/teachers/{id}", savedTeacher.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(teacherJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Sangeetha"))
			.andExpect(jsonPath("$.lastName").value("VijayKrishnan"))
			.andExpect(jsonPath("$.phoneNumber").value("9732621578"));
		
		// Clean up
		teacherRepository.delete(savedTeacher.getId());
	}
	
	@SneakyThrows
	@Test
	public void deleteTeacherShouldSucceed() {
		// Given an existing teacher
		Teacher teacher = new Teacher();
		teacher.setFirstName("Sangeetha");
		teacher.setLastName("Chandrashekhar");
		teacher.setPhoneNumber("9732621578");
		Teacher savedTeacher = teacherRepository.save(teacher);
		
		// When I delete the teacher
		// Then it should succeed
		mockMvc.perform(delete("/teachers/{id}", savedTeacher.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}