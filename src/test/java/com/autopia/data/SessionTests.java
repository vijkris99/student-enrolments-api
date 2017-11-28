package com.autopia.data;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.autopia.data.entities.Session;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.SessionRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class SessionTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	private Enrolment enrolment1;
	
	private ZonedDateTime startTime = ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
														LocalTime.of(11, 00), ZoneId.of("Etc/UTC"));
	
	private ZonedDateTime endTime = ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
														LocalTime.of(11, 30), ZoneId.of("Etc/UTC"));
	
	@Before
	public void setup() {
		Teacher teacher1 = new Teacher();
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9876543210");
		entityManager.persist(teacher1);
		
		Skill skill1 = new Skill();
		skill1.setName("Keyboard");
		entityManager.persist(skill1);
		
		Student student1 = new Student();
		student1.setFirstName("Mythri");
		student1.setLastName("Arjun");
		student1.setPhoneNumber("1234567890");
		entityManager.persist(student1);
		
		enrolment1 = new Enrolment();
		enrolment1.setTeacher(teacher1);
		enrolment1.setSkill(skill1);
		enrolment1.setStudent(student1);
		enrolment1.setSessionFee(20);
		entityManager.persist(enrolment1);
		
		Session session1 = new Session();
		session1.setEnrolment(enrolment1);
		session1.setStartTime(startTime);
		session1.setEndTime(endTime);
		entityManager.persist(session1);
		
		entityManager.flush();
	}
	
	@Test
	public void insertNewSessionShouldSucceed() {
		Session session2 = new Session();
		session2.setEnrolment(enrolment1);
		session2.setStartTime(startTime.plusMinutes(30));
		session2.setEndTime(endTime.plusMinutes(30));
		Session savedSession2 = sessionRepository.save(session2);
		
		assertThat(savedSession2.getEnrolment()).isEqualTo(enrolment1);
		assertThat(savedSession2.getStartTime()).isEqualTo(startTime.plusMinutes(30));
		assertThat(savedSession2.getEndTime()).isEqualTo(endTime.plusMinutes(30));
		assertThat(savedSession2.getCompleted()).isEqualTo(false);
		assertThat(savedSession2.getFeePaid()).isEqualTo(0);
	}
	
	@Test
	public void findByStartTimeShouldSucceed() {
		Session foundSession = sessionRepository.findByStartTime(startTime);
		
		assertThat(foundSession).as("Found session should not be null").isNotNull();
		assertThat(foundSession.getEnrolment()).isEqualTo(enrolment1);
		assertThat(foundSession.getStartTime()).isEqualTo(startTime);
		assertThat(foundSession.getEndTime()).isEqualTo(endTime);
		assertThat(foundSession.getCompleted()).isEqualTo(false);
		assertThat(foundSession.getFeePaid()).isEqualTo(0);
	}
	
	@Test
	public void findByStartTimeAndEndTimeShouldSucceed() {
		Session foundSession = sessionRepository.findByStartTimeAndEndTime(startTime, endTime);
		
		assertThat(foundSession).as("Found session should not be null").isNotNull();
		assertThat(foundSession.getEnrolment()).isEqualTo(enrolment1);
		assertThat(foundSession.getStartTime()).isEqualTo(startTime);
		assertThat(foundSession.getEndTime()).isEqualTo(endTime);
		assertThat(foundSession.getCompleted()).isEqualTo(false);
		assertThat(foundSession.getFeePaid()).isEqualTo(0);
	}
	
	@Test
	public void findByEnrolmentShouldSucceed() {
		List<Session> foundSessions = sessionRepository.findByEnrolment(enrolment1);
		assertThat(foundSessions.size()).isEqualTo(1);
		Session foundSession = foundSessions.get(0);
		
		assertThat(foundSession.getEnrolment()).isEqualTo(enrolment1);
		assertThat(foundSession.getStartTime()).isEqualTo(startTime);
		assertThat(foundSession.getEndTime()).isEqualTo(endTime);
		assertThat(foundSession.getCompleted()).isEqualTo(false);
		assertThat(foundSession.getFeePaid()).isEqualTo(0);
	}
	
	@Test
	public void findBySessionCompletedShouldSucceed() {
		List<Session> foundSessions = sessionRepository.findByCompleted(false);
		assertThat(foundSessions.size()).isEqualTo(1);
		Session foundSession = foundSessions.get(0);
		
		assertThat(foundSession).as("Found session should not be null").isNotNull();
		assertThat(foundSession.getEnrolment()).isEqualTo(enrolment1);
		assertThat(foundSession.getStartTime()).isEqualTo(startTime);
		assertThat(foundSession.getEndTime()).isEqualTo(endTime);
		assertThat(foundSession.getCompleted()).isEqualTo(false);
		assertThat(foundSession.getFeePaid()).isEqualTo(0);
	}
	
	@Test
	public void countBySessionCompletedShouldSucceed() {
		Long countSessionsCompleted = sessionRepository.countByCompleted(false);
		assertThat(countSessionsCompleted).isEqualTo(1);
		
		Long countSessionsNotCompleted = sessionRepository.countByCompleted(true);
		assertThat(countSessionsNotCompleted).isEqualTo(0);
	}
}