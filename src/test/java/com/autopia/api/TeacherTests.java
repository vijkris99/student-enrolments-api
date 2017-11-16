package com.autopia.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.TeacherRepository;

import lombok.SneakyThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Before
	public void setup() {
		teacherRepository.deleteAll();
		
		Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9732621588");
		teacherRepository.save(teacher);
	}
	
	@SneakyThrows
	@Test
	public void getTeacherShouldReturnCorrectData() {
		mockMvc.perform(get("/teachers/{id}", 1))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Vijay"));
	}
	
	@SneakyThrows
	@Test
	public void getTeachersShouldSucceed() {
		mockMvc.perform(get("/teachers"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists());
	}
	
	@SneakyThrows
	@Test
	public void createTeacherShouldSucceed() {
		/*Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9732621588");*/
		
		//ObjectMapper objectMapper = new ObjectMapper();
		//log.info(objectMapper.writeValueAsString(teacher));
		
		String teacherJson = "{\"firstName\":\"Vijay\",\"lastName\":\"Ramaswamy\",\"phoneNumber\":\"9732621588\"}";
		
		mockMvc
			.perform(post("/teachers")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(teacherJson))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Vijay"));
	}
}