package com.autopia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Skill;
import com.autopia.data.repositories.SkillRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class SkillTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private SkillRepository skillRepository;
	
	
	@Before
	public void setup() {
		Skill skill = new Skill();
		skill.setName("Keyboard");
		entityManager.persist(skill);
	}
	
	@Test
	public void insertNewSkillShouldSucceed() {
		Skill skill = new Skill();
		skill.setName("Guitar");
		Skill foundSkill = skillRepository.save(skill);
		
		assertThat(foundSkill.getName()).isEqualTo("Guitar");
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void skillNameShouldBeUnique() {
		Skill skill = new Skill();
		skill.setName("Keyboard");
		skillRepository.save(skill);
	}
	
	@Test
	public void findBySkillNameShouldWork() {
		List<Skill> foundSkills = skillRepository.findByName("Keyboard");
		assertThat(foundSkills.size()).isEqualTo(1);
		
		Skill foundSkill = foundSkills.get(0);
		assertThat(foundSkill.getName()).isEqualTo("Keyboard");
	}
	
	@Test
	public void findBySkillNameShouldReturnEmptyListWhenNotFound() {
		List<Skill> foundSkills = skillRepository.findByName("Vocals");
		assertThat(foundSkills.size()).isEqualTo(0);
	}
}