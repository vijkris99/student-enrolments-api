package com.autopia.api;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.EnrolmentRepository;
import com.autopia.data.repositories.SkillRepository;
import com.autopia.data.repositories.StudentRepository;
import com.autopia.data.repositories.TeacherRepository;

public abstract class BaseTest {
	
	@Autowired
	protected TeacherRepository teacherRepository;
	
	@Autowired
	protected SkillRepository skillRepository;
	
	@Autowired
	protected StudentRepository studentRepository;
	
	@Autowired
	protected EnrolmentRepository enrolmentRepository;
	
	protected static Teacher teacher1 = new Teacher();
	protected static Teacher teacher2 = new Teacher();
	protected static Skill skill1 = new Skill();
	protected static Skill skill2 = new Skill();
	protected static Student student1 = new Student();
	protected static Student student2 = new Student();
	protected static Enrolment enrolment1 = new Enrolment();
	
	protected static Boolean dataInitialized = false;
	
	@Before
	public void setup() {
		if(!dataInitialized) {
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
			
			enrolment1.setTeacher(teacher1);
			enrolment1.setSkill(skill1);
			enrolment1.setStudent(student1);
			enrolment1.setSessionFee(20);
			enrolment1.setIsActive(true);
			
			teacherRepository.deleteAll();
			skillRepository.deleteAll();
			studentRepository.deleteAll();
			
			teacherRepository.save(teacher1);
			teacherRepository.save(teacher2);
			skillRepository.save(skill1);
			skillRepository.save(skill2);
			studentRepository.save(student1);
			studentRepository.save(student2);
			enrolmentRepository.save(enrolment1);
			
			dataInitialized = true;
		}
	}
}