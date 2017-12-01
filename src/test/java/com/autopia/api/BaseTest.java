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
	
	protected Teacher savedTeacher1;
	protected Teacher savedTeacher2;
	protected Skill savedSkill1;
	protected Skill savedSkill2;
	protected Student savedStudent1;
	protected Student savedStudent2;
	protected Enrolment savedEnrolment1;
	protected Session savedSession1;
	
	@Before
	public void setup() {
		Teacher teacher1 = new Teacher();
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9732621588");
		savedTeacher1 = teacherRepository.save(teacher1);
		
		Teacher teacher2 = new Teacher();
		teacher2.setFirstName("Madhav");
		teacher2.setLastName("Bhatki");
		teacher2.setPhoneNumber("1234567890");
		savedTeacher2 = teacherRepository.save(teacher2);
		
		Skill skill1 = new Skill();
		skill1.setName("Keyboard");
		savedSkill1 = skillRepository.save(skill1);
		
		Skill skill2 = new Skill();
		skill2.setName("Guitar");
		savedSkill2 = skillRepository.save(skill2);
		
		Student student1 = new Student();
		student1.setFirstName("Pranav");
		student1.setLastName("Kishore");
		savedStudent1 = studentRepository.save(student1);
		
		Student student2 = new Student();
		student2.setFirstName("Arwin");
		student2.setLastName("Balaji");
		savedStudent2 = studentRepository.save(student2);
		
		Enrolment enrolment1 = new Enrolment();
		enrolment1.setTeacher(teacher1);
		enrolment1.setSkill(skill1);
		enrolment1.setStudent(student1);
		enrolment1.setSessionFee(20);
		savedEnrolment1 = enrolmentRepository.save(enrolment1);
		
		Session session1 = new Session();
		session1.setEnrolment(enrolment1);
		// Refer https://en.wikipedia.org/wiki/List_of_tz_database_time_zones for Zone ID list
		session1.setStartTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(12, 00), ZoneId.of("Etc/UTC")));
		session1.setEndTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(12, 30), ZoneId.of("Etc/UTC")));
		savedSession1 = sessionRepository.save(session1);
	}
}