package com.autopia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.TeacherRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class TeacherTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	
	@Before
	public void setup() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Vijay");
		teacher.setLastName("Ramaswamy");
		teacher.setPhoneNumber("9876543210");
		entityManager.persist(teacher);
	}
	
	@Test
	public void insertNewTeacherShouldSucceed() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Madhav");
		teacher.setLastName("Bhatki");
		Teacher foundTeacher = teacherRepository.save(teacher);
		
		assertThat(foundTeacher.getFirstName()).isEqualTo("Madhav");
		assertThat(foundTeacher.getLastName()).isEqualTo("Bhatki");
	}
	
	@Test
	public void findByFirstNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByFirstName("Vijay");
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher = foundTeachers.get(0);
		assertThat(foundTeacher.getFirstName()).isEqualTo("Vijay");
		assertThat(foundTeacher.getLastName()).isEqualTo("Ramaswamy");
		assertThat(foundTeacher.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByLastNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByLastName("Ramaswamy");
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher = foundTeachers.get(0);
		assertThat(foundTeacher.getFirstName()).isEqualTo("Vijay");
		assertThat(foundTeacher.getLastName()).isEqualTo("Ramaswamy");
		assertThat(foundTeacher.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByFirstNameAndLastNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByFirstNameAndLastName("Vijay", "Ramaswamy");
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher = foundTeachers.get(0);
		assertThat(foundTeacher.getFirstName()).isEqualTo("Vijay");
		assertThat(foundTeacher.getLastName()).isEqualTo("Ramaswamy");
		assertThat(foundTeacher.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByPhoneNumberShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByPhoneNumber("9876543210");
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher = foundTeachers.get(0);
		assertThat(foundTeacher.getFirstName()).isEqualTo("Vijay");
		assertThat(foundTeacher.getLastName()).isEqualTo("Ramaswamy");
		assertThat(foundTeacher.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByPhoneNumberShouldReturnEmptyListWhenNotFound() {
		List<Teacher> foundTeachers = teacherRepository.findByPhoneNumber("1234567890");
		assertThat(foundTeachers.size()).isEqualTo(0);
	}
}