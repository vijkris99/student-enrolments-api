package com.autopia.api;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.autopia.data.entities.Session;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class SessionTests extends BaseTest {
	
	@SneakyThrows
	@Test
	public void getSessionsShouldSucceed() {
		mockMvc.perform(get("/sessions"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists())
						.andExpect(jsonPath("$._embedded.sessions").isArray())
						.andExpect(jsonPath("$._embedded.sessions.length()").value(1));
	}
	
	@SneakyThrows
	@Test
	public void getSessionShouldReturnCorrectData() {
		// Given an existing session
		
		// When I read the session
		// Then I should succeed
		// And the session should contain a link to the corresponding enrolment
		mockMvc
			.perform(get("/sessions/{id}", savedSession1.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.enrolment.href").exists())
			.andExpect(jsonPath("$.startTime").value("2017-11-22T12:00:00Z"))	// always return UTC
			.andExpect(jsonPath("$.endTime").value("2017-11-22T12:30:00Z"))	// always return UTC
			.andExpect(jsonPath("$.completed").value(false))
			.andExpect(jsonPath("$.feePaid").value(0));
	}
	
	@SneakyThrows
	@Test
	public void createSessionShouldSucceed() {
		// Given a new session
		ObjectNode sessionJson = objectMapper.createObjectNode();
		sessionJson.put("enrolment", "/enrolments/" + savedEnrolment1.getId());
		sessionJson.put("startTime", "2017-11-22T12:00:00-05:00");	// Accept any time-zone
		sessionJson.put("endTime", "2017-11-22T12:30:00-05:00");
		
		// When I create a new entry for the session
		// Then it should succeed
		MvcResult result = null;
		try {
			result = mockMvc
						.perform(post("/sessions")
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.content(sessionJson.toString()))
						.andDo(print())
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$._links.enrolment.href").exists())
						.andExpect(jsonPath("$.startTime").value("2017-11-22T17:00:00Z"))	// Always return UTC
						.andExpect(jsonPath("$.endTime").value("2017-11-22T17:30:00Z"))
						.andExpect(jsonPath("$.completed").value(false))
						.andExpect(jsonPath("$.feePaid").value(0))
						.andReturn();
		} catch(Exception ex) {
			log.error("Error creating a new session", ex);
		} finally {
			// Clean up
			String createdSessionLocation = result.getResponse().getHeader("Location");
			long createdSessionId = Long.parseLong(
									createdSessionLocation.replace("http://localhost/sessions/", ""));
			sessionRepository.delete(createdSessionId);
		}
	}
	
	@SneakyThrows
	@Test
	public void findByEnrolmentIdShouldWork() {
		mockMvc
			.perform(get("/sessions/search/findByEnrolmentId")
					.param("enrolmentId", savedSession1.getEnrolment().getId().toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.sessions[0]_links.self.href",
							containsString("/sessions/" + savedSession1.getId())));
	}
	
	@Ignore
	@SneakyThrows
	@Test
	public void findByStartTimeShouldWork() {
		mockMvc
			.perform(get("/sessions/search/findByStartTime")
					.param("startTime", savedSession1.getStartTime().toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.sessions[0]_links.self.href",
							containsString("/sessions/" + savedSession1.getId())));
	}
	
	@Ignore
	@SneakyThrows
	@Test
	public void findByStartTimeAndEndTimeShouldWork() {
		mockMvc
			.perform(get("/sessions/search/findByStartTimeAndEndTime")
					.param("startTime", savedSession1.getStartTime().toString())
					.param("endTime", savedSession1.getEndTime().toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.sessions[0]_links.self.href",
							containsString("/sessions/" + savedSession1.getId())));
	}
	
	@SneakyThrows
	@Test
	public void findByCompletedShouldWork() {
		mockMvc
			.perform(get("/sessions/search/findByCompleted")
					.param("completed", savedSession1.getCompleted().toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.sessions[0]_links.self.href",
							containsString("/sessions/" + savedSession1.getId())));
	}
	
	@SneakyThrows
	@Test
	public void countByCompletedShouldSucceed() {
		mockMvc.perform(get("/sessions/search/countByCompleted")
							.param("completed", "false"))
				.andDo(print())
				.andExpect(content().string("1"));
		
		mockMvc.perform(get("/sessions/search/countByCompleted")
							.param("completed", "true"))
				.andDo(print())
				.andExpect(content().string("0"));
	}
	
	@SneakyThrows
	@Test
	public void replaceSessionShouldSucceed() {
		// Given an existing session
		Session session = new Session();
		session.setEnrolment(savedEnrolment1);
		session.setStartTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 00), ZoneId.of("Etc/UTC")));
		session.setEndTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 30), ZoneId.of("Etc/UTC")));
		Session savedSession = sessionRepository.save(session);
		
		// When I replace the existing entry for the session
		// Then it should succeed
		ObjectNode sessionJson = objectMapper.createObjectNode();
		sessionJson.put("enrolment", "/enrolments/" + savedEnrolment1.getId());
		sessionJson.put("startTime", "2017-11-22T12:00:00-05:00");
		sessionJson.put("endTime", "2017-11-22T12:30:00-05:00");
		
		try {
			mockMvc
			.perform(put("/sessions/{id}", savedSession.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(sessionJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.enrolment.href").exists())
			.andExpect(jsonPath("$.startTime").value("2017-11-22T17:00:00Z"))
			.andExpect(jsonPath("$.endTime").value("2017-11-22T17:30:00Z"))
			.andExpect(jsonPath("$.completed").value(false))
			.andExpect(jsonPath("$.feePaid").value(0));
		} catch(Exception ex) {
			log.error("Error replacing an existing session", ex);
		} finally {
			// Clean up
			sessionRepository.delete(savedSession.getId());
		}
	}
	
	@SneakyThrows
	@Test
	public void updateSessionShouldSucceed() {
		// Given an existing session
		Session session = new Session();
		session.setEnrolment(savedEnrolment1);
		session.setStartTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 00), ZoneId.of("Etc/UTC")));
		session.setEndTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 30), ZoneId.of("Etc/UTC")));
		Session savedSession = sessionRepository.save(session);
		
		// When I update the existing entry for the session
		// Then it should succeed
		ObjectNode sessionJson = objectMapper.createObjectNode();
		sessionJson.put("completed", true);
		sessionJson.put("feePaid", 20);
		
		try {
			mockMvc
			.perform(patch("/sessions/{id}", savedSession.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(sessionJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.enrolment.href").exists())
			.andExpect(jsonPath("$.startTime").value("2017-11-22T11:00:00Z"))
			.andExpect(jsonPath("$.endTime").value("2017-11-22T11:30:00Z"))
			.andExpect(jsonPath("$.completed").value(true))
			.andExpect(jsonPath("$.feePaid").value(20));
		} catch(Exception ex) {
			log.error("Error updating an existing session", ex);
		} finally {
			// Clean up
			sessionRepository.delete(savedSession.getId());
		}
	}
	
	@SneakyThrows
	@Test
	public void deleteSessionShouldSucceed() {
		// Given an existing session
		Session session = new Session();
		session.setEnrolment(savedEnrolment1);
		session.setStartTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 00), ZoneId.of("Etc/UTC")));
		session.setEndTime(ZonedDateTime.of(LocalDate.of(2017, Month.NOVEMBER, 22),
												LocalTime.of(11, 30), ZoneId.of("Etc/UTC")));
		Session savedSession = sessionRepository.save(session);
		
		// When I delete the session
		// Then it should succeed
		mockMvc.perform(delete("/sessions/{id}", savedSession.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// And the session should no longer be found
		Session foundSession = sessionRepository.findOne(savedSession.getId());
		assertThat(foundSession, Matchers.nullValue());
	}
}