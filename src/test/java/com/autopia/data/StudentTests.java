package com.autopia.data;

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


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class StudentTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Test
	public void insertNewStudentShouldSucceed() {
		Student student = new Student();
		student.setFirstName("Mythri");
		student.setLastName("Arjun");
		entityManager.persist(student);
		
		Student foundStudent = studentRepository.findOne((long) 1);
		assertThat(foundStudent.getFirstName()).isEqualTo("Mythri");
		assertThat(foundStudent.getLastName()).isEqualTo("Arjun");
	}
}