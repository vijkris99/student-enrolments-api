package com.autopia.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Skill;
import com.autopia.data.repositories.SkillRepository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class SkillTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private SkillRepository skillRepository;
	
	private Skill skill1;
	
	@Before
	public void setup() {
		skill1 = new Skill();
		skill1.setName("Keyboard");
		entityManager.persist(skill1);
	}
	
	@Test
	public void insertNewSkillShouldSucceed() {
		Skill skill2 = new Skill();
		skill2.setName("Guitar");
		Skill savedSkill2 = skillRepository.save(skill2);
		
		assertThat(savedSkill2.getName()).isEqualTo("Guitar");
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void skillNameShouldBeUnique() {
		Skill skill1Dupe = new Skill();
		skill1Dupe.setName("Keyboard");
		skillRepository.save(skill1Dupe);
	}
	
	@Test
	public void findBySkillNameShouldWork() {
		List<Skill> foundSkills = skillRepository.findByName(skill1.getName());
		assertThat(foundSkills.size()).isEqualTo(1);
		
		Skill foundSkill1 = foundSkills.get(0);
		assertThat(foundSkill1.getName()).isEqualTo(skill1.getName());
	}
	
	@Test
	public void findBySkillNameShouldReturnEmptyListWhenNotFound() {
		List<Skill> foundSkills = skillRepository.findByName("Vocals");
		assertThat(foundSkills.size()).isEqualTo(0);
	}
}