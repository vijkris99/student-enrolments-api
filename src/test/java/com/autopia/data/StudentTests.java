package com.autopia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Student;
import com.autopia.data.repositories.StudentRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class StudentTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private StudentRepository studentRepository;
	
	
	@Before
	public void setup() {
		Student student = new Student();
		student.setFirstName("Mythri");
		student.setLastName("Arjun");
		student.setPhoneNumber("9876543210");
		entityManager.persist(student);
	}
	
	@Test
	public void insertNewStudentShouldSucceed() {
		Student student = new Student();
		student.setFirstName("Pranav");
		student.setLastName("Kishore");
		Student foundStudent = studentRepository.save(student);
		
		assertThat(foundStudent.getFirstName()).isEqualTo("Pranav");
		assertThat(foundStudent.getLastName()).isEqualTo("Kishore");
	}
	
	@Test
	public void findByFirstNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstName("Mythri");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByFirstNameIgnoreCaseShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameIgnoreCase("mythri");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByLastNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByLastName("Arjun");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByFirstNameAndLastNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameAndLastName("Mythri", "Arjun");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByFirstNameAndLastNameAllIgnoreCaseShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameAndLastNameAllIgnoreCase("mythrI", "aRjun");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByPhoneNumberShouldWork() {
		List<Student> foundStudents = studentRepository.findByPhoneNumber("9876543210");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent = foundStudents.get(0);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
		assertThat(foundStudent.getPhoneNumber()).isEqualTo("9876543210");
	}
	
	@Test
	public void findByPhoneNumberShouldReturnEmptyListWhenNotFound() {
		List<Student> foundStudents = studentRepository.findByPhoneNumber("1234567890");
		assertThat(foundStudents.size()).isEqualTo(0);
	}
}