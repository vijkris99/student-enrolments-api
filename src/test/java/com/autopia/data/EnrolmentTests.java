package com.autopia.data;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.EnrolmentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class EnrolmentTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private EnrolmentRepository enrolmentRepository;
	
	private Teacher teacher1;
	private Skill skill1, skill2;
	private Student student1;
	
	@Before
	public void setup() {
		teacher1 = new Teacher();
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9876543210");
		entityManager.persist(teacher1);
		
		skill1 = new Skill();
		skill1.setName("Keyboard");
		entityManager.persist(skill1);
		
		skill2 = new Skill();
		skill2.setName("Guitar");
		entityManager.persist(skill2);
		
		student1 = new Student();
		student1.setFirstName("Mythri");
		student1.setLastName("Arjun");
		student1.setPhoneNumber("1234567890");
		entityManager.persist(student1);
		
		Enrolment enrolment1 = new Enrolment();
		enrolment1.setTeacher(teacher1);
		enrolment1.setSkill(skill1);
		enrolment1.setStudent(student1);
		enrolment1.setSessionFee(20);
		entityManager.persist(enrolment1);
		
		entityManager.flush();
	}
	
	@Test
	public void insertNewEnrolmentShouldWork() {
		Enrolment enrolment2 = new Enrolment();
		enrolment2.setTeacher(teacher1);
		enrolment2.setSkill(skill2);
		enrolment2.setStudent(student1);
		enrolment2.setSessionFee(20);
		
		Enrolment foundEnrolment2 = enrolmentRepository.save(enrolment2);
		
		assertThat(foundEnrolment2.getSkill()).isEqualTo(skill2);
		assertThat(foundEnrolment2.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment2.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment2.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment2.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment2.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findBySkillShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findBySkill(skill1);
		assertThat(foundEnrolments.size()).isEqualTo(1);
		
		Enrolment foundEnrolment = foundEnrolments.get(0);
		assertThat(foundEnrolment.getSkill()).isEqualTo(skill1);
		assertThat(foundEnrolment.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findBySkillNameShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findBySkillName(skill1.getName());
		assertThat(foundEnrolments.size()).isEqualTo(1);
		
		Enrolment foundEnrolment = foundEnrolments.get(0);
		assertThat(foundEnrolment.getSkill()).isEqualTo(skill1);
		assertThat(foundEnrolment.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findByTeacherShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findByTeacher(teacher1);
		assertThat(foundEnrolments.size()).isEqualTo(1);
		
		Enrolment foundEnrolment = foundEnrolments.get(0);
		assertThat(foundEnrolment.getSkill()).isEqualTo(skill1);
		assertThat(foundEnrolment.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findByStudentShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findByStudent(student1);
		assertThat(foundEnrolments.size()).isEqualTo(1);
		
		Enrolment foundEnrolment = foundEnrolments.get(0);
		assertThat(foundEnrolment.getSkill()).isEqualTo(skill1);
		assertThat(foundEnrolment.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findByIsActiveShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findByIsActive(true);
		assertThat(foundEnrolments.size()).isEqualTo(1);
		
		Enrolment foundEnrolment = foundEnrolments.get(0);
		assertThat(foundEnrolment.getSkill()).isEqualTo(skill1);
		assertThat(foundEnrolment.getTeacher()).isEqualTo(teacher1);
		assertThat(foundEnrolment.getStudent()).isEqualTo(student1);
		assertThat(foundEnrolment.getSessionFee()).isEqualTo(20);
		assertThat(foundEnrolment.getIsActive()).isEqualTo(true);
		assertThat(foundEnrolment.getBalanceDue()).isEqualTo(0);
	}
	
	@Test
	public void findByBalanceDueGreaterThanShouldWork() {
		List<Enrolment> foundEnrolments = enrolmentRepository.findByBalanceDueGreaterThan(0);
		assertThat(foundEnrolments.size()).isEqualTo(0);
	}
}