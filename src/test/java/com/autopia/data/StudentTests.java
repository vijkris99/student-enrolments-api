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
	
	private Student student1;
	
	@Before
	public void setup() {
		student1 = new Student();
		student1.setFirstName("Mythri");
		student1.setLastName("Arjun");
		student1.setPhoneNumber("9876543210");
		entityManager.persist(student1);
	}
	
	@Test
	public void insertNewStudentShouldSucceed() {
		Student student2 = new Student();
		student2.setFirstName("Pranav");
		student2.setLastName("Kishore");
		Student savedStudent2 = studentRepository.save(student2);
		
		assertThat(savedStudent2.getFirstName()).isEqualTo("Pranav");
		assertThat(savedStudent2.getLastName()).isEqualTo("Kishore");
	}
	
	@Test
	public void findByFirstNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstName(student1.getFirstName());
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByFirstNameIgnoreCaseShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameIgnoreCase("mythri");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByLastNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByLastName(student1.getLastName());
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByFirstNameAndLastNameShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameAndLastName(student1.getFirstName(),
																					student1.getLastName());
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByFirstNameAndLastNameAllIgnoreCaseShouldWork() {
		List<Student> foundStudents = studentRepository.findByFirstNameAndLastNameAllIgnoreCase("mythrI", "aRjun");
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByPhoneNumberShouldWork() {
		List<Student> foundStudents = studentRepository.findByPhoneNumber(student1.getPhoneNumber());
		assertThat(foundStudents.size()).isEqualTo(1);
		
		Student foundStudent1 = foundStudents.get(0);
		assertThat(foundStudent1.getFirstName()).isEqualTo(student1.getFirstName());
		assertThat(foundStudent1.getLastName()).isEqualTo(student1.getLastName());
		assertThat(foundStudent1.getPhoneNumber()).isEqualTo(student1.getPhoneNumber());
	}
	
	@Test
	public void findByPhoneNumberShouldReturnEmptyListWhenNotFound() {
		List<Student> foundStudents = studentRepository.findByPhoneNumber("1234567890");
		assertThat(foundStudents.size()).isEqualTo(0);
	}
}