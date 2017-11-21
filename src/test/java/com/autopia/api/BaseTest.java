package com.autopia.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Session;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.EnrolmentRepository;
import com.autopia.data.repositories.SessionRepository;
import com.autopia.data.repositories.SkillRepository;
import com.autopia.data.repositories.StudentRepository;
import com.autopia.data.repositories.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseTest {
	

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	@Autowired
	protected TeacherRepository teacherRepository;
	
	@Autowired
	protected SkillRepository skillRepository;
	
	@Autowired
	protected StudentRepository studentRepository;
	
	@Autowired
	protected EnrolmentRepository enrolmentRepository;
	
	@Autowired
	protected SessionRepository sessionRepository;
	
	protected static Teacher teacher1 = new Teacher();
	protected static Teacher teacher2 = new Teacher();
	protected static Skill skill1 = new Skill();
	protected static Skill skill2 = new Skill();
	protected static Student student1 = new Student();
	protected static Student student2 = new Student();
	protected static Enrolment enrolment1 = new Enrolment();
	protected static Session session1 = new Session();
	
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
			
			session1.setEnrolment(enrolment1);
			// Use ZoneId of Etc/UTC for UTC
			// Use America/New_York for EST
			session1.setStartTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
													LocalTime.of(12, 00), ZoneId.of("Etc/UTC")));
			session1.setEndTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
													LocalTime.of(12, 30), ZoneId.of("Etc/UTC")));
			
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
			sessionRepository.save(session1);
			
			dataInitialized = true;
		}
	}
}