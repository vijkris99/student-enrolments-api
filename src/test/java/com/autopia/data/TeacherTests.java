package com.autopia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.TeacherRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TeacherTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	private Teacher teacher1;
	
	@Before
	public void setup() {
		teacher1 = new Teacher();
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9876543210");
		entityManager.persist(teacher1);
	}
	
	@Test
	public void getTeachersShouldSucceed() {
		List<Teacher> foundTeachers = (List<Teacher>) teacherRepository.findAll();
		assertThat(foundTeachers.size()).isEqualTo(1);
	}
	
	@Test
	public void insertNewTeacherShouldSucceed() {
		Teacher teacher2 = new Teacher();
		teacher2.setFirstName("Madhav");
		teacher2.setLastName("Bhatki");
		Teacher foundTeacher = teacherRepository.save(teacher2);
		
		assertThat(foundTeacher.getFirstName()).isEqualTo("Madhav");
		assertThat(foundTeacher.getLastName()).isEqualTo("Bhatki");
	}
	
	@Test
	public void findByFirstNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByFirstName(teacher1.getFirstName());
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher1 = foundTeachers.get(0);
		assertThat(foundTeacher1.getFirstName()).isEqualTo(teacher1.getFirstName());
		assertThat(foundTeacher1.getLastName()).isEqualTo(teacher1.getLastName());
		assertThat(foundTeacher1.getPhoneNumber()).isEqualTo(teacher1.getPhoneNumber());
	}
	
	@Test
	public void findByLastNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByLastName(teacher1.getLastName());
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher1 = foundTeachers.get(0);
		assertThat(foundTeacher1.getFirstName()).isEqualTo(teacher1.getFirstName());
		assertThat(foundTeacher1.getLastName()).isEqualTo(teacher1.getLastName());
		assertThat(foundTeacher1.getPhoneNumber()).isEqualTo(teacher1.getPhoneNumber());
	}
	
	@Test
	public void findByFirstNameAndLastNameShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByFirstNameAndLastName(teacher1.getFirstName(),
																					teacher1.getLastName());
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher1 = foundTeachers.get(0);
		assertThat(foundTeacher1.getFirstName()).isEqualTo(teacher1.getFirstName());
		assertThat(foundTeacher1.getLastName()).isEqualTo(teacher1.getLastName());
		assertThat(foundTeacher1.getPhoneNumber()).isEqualTo(teacher1.getPhoneNumber());
	}
	
	@Test
	public void findByPhoneNumberShouldWork() {
		List<Teacher> foundTeachers = teacherRepository.findByPhoneNumber(teacher1.getPhoneNumber());
		assertThat(foundTeachers.size()).isEqualTo(1);
		
		Teacher foundTeacher1 = foundTeachers.get(0);
		assertThat(foundTeacher1.getFirstName()).isEqualTo(teacher1.getFirstName());
		assertThat(foundTeacher1.getLastName()).isEqualTo(teacher1.getLastName());
		assertThat(foundTeacher1.getPhoneNumber()).isEqualTo(teacher1.getPhoneNumber());
	}
	
	@Test
	public void findByPhoneNumberShouldReturnEmptyListWhenNotFound() {
		List<Teacher> foundTeachers = teacherRepository.findByPhoneNumber("1234567890");
		assertThat(foundTeachers.size()).isEqualTo(0);
	}
	
	@Test
	public void deleteTeacherShouldSucceed() {
		teacherRepository.delete(teacherRepository.findByFirstName(teacher1.getFirstName()));
		
		List<Teacher> foundTeachers = (List<Teacher>) teacherRepository.findAll();
		assertThat(foundTeachers.size()).isEqualTo(0);
	}
}