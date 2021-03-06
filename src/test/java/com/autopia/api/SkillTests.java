package com.autopia.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.autopia.data.entities.Skill;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
public class SkillTests extends BaseTest {
	
	@SneakyThrows
	@Test
	public void getSkillsShouldSucceed() {
		mockMvc.perform(get("/skills"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$._embedded").exists())
						.andExpect(jsonPath("$._links.self").exists())
						.andExpect(jsonPath("$._links.profile").exists())
						.andExpect(jsonPath("$._embedded.skills").isArray())
						.andExpect(jsonPath("$._embedded.skills.length()").value(2));
	}
	
	@SneakyThrows
	@Test
	public void getSkillShouldReturnCorrectData() {
		// Given an existing skill
		
		// When I read the skill
		// Then I should succeed
		// And the skill details should be accurate
		mockMvc.perform(get("/skills/{id}", savedSkill1.getId()))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.name").value(savedSkill1.getName()));
	}
	
	@SneakyThrows
	@Test
	public void createSkillShouldSucceed() {
		// Given a new skill
		String skillJson = "{\"name\":\"Vocals\"}";
		
		// When I create a new entry for the skill
		// Then it should succeed
		MvcResult result = null;
		try {
			result = mockMvc
						.perform(post("/skills")
								.contentType(MediaType.APPLICATION_JSON_UTF8)
								.accept(MediaType.APPLICATION_JSON_UTF8)
								.content(skillJson))
						.andDo(print())
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.name", is("Vocals")))
						.andReturn();
		} catch(Exception ex) {
			log.error("Error creating a new skill", ex);
		} finally {
			// Clean up
			String createdSkillLocation = result.getResponse().getHeader("Location");
			long createdSkillId = Long.parseLong(
									createdSkillLocation.replace("http://localhost/skills/", ""));
			skillRepository.delete(createdSkillId);
		}
	}
	
	@SneakyThrows
	@Test
	public void findByNameShouldWork() {
		mockMvc
			.perform(get("/skills/search/findByName")
					.param("name", savedSkill1.getName()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.skills[0]_links.self.href",
											containsString("/skills/" + savedSkill1.getId())));
	}
	
	@SneakyThrows
	@Test
	public void replaceSkillShouldSucceed() {
		// Given an existing skill
		Skill skill = new Skill();
		skill.setName("Vocals");
		Skill savedSkill = skillRepository.save(skill);
		
		// When I replace the existing entry for the skill
		// Then it should succeed
		ObjectNode skillJson = objectMapper.createObjectNode();
		skillJson.put("name", "Violin");
		
		try {
			mockMvc
			.perform(put("/skills/{id}", savedSkill.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(skillJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Violin"));
		} catch(Exception ex) {
			log.error("Error replacing an existing skill", ex);
		} finally {
			// Clean up
			skillRepository.delete(savedSkill.getId());
		}
	}
	
	@SneakyThrows
	@Test
	public void updateSkillShouldSucceed() {
		// Given an existing skill
		Skill skill = new Skill();
		skill.setName("Vocals");
		Skill savedSkill = skillRepository.save(skill);
		
		// When I update the existing entry for the skill
		// Then it should succeed
		ObjectNode skillJson = objectMapper.createObjectNode();
		skillJson.put("name", "Violin");
		
		try {
			mockMvc
			.perform(patch("/skills/{id}", savedSkill.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(skillJson.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Violin"));
		} catch(Exception ex) {
			log.error("Error updating an existing skill", ex);
		} finally {
			// Clean up
			skillRepository.delete(savedSkill.getId());
		}
	}
	
	@SneakyThrows
	@Test
	public void deleteSkillShouldSucceed() {
		// Given an existing skill
		Skill skill = new Skill();
		skill.setName("Vocals");
		Skill savedSkill = skillRepository.save(skill);
		
		// When I delete the skill
		// Then it should succeed
		mockMvc.perform(delete("/skills/{id}", savedSkill.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
		
		// And the skill should no longer be found
		Skill foundSkill = skillRepository.findOne(savedSkill.getId());
		assertThat(foundSkill, Matchers.nullValue());
	}
}