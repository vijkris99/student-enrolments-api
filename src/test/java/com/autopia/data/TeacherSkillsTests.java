package com.autopia.data;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Teacher;
import com.autopia.data.repositories.SkillRepository;
import com.autopia.data.repositories.TeacherRepository;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
public class TeacherSkillsTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private SkillRepository skillRepository;
	
	
	@Before
	public void setup() {
		Skill skill1 = new Skill();
		skill1.setName("Keyboard");
		entityManager.persist(skill1);
		
		Skill skill2 = new Skill();
		skill2.setName("Guitar");
		entityManager.persist(skill2);
		
		Teacher teacher1 = new Teacher();
		teacher1.setFirstName("Vijay");
		teacher1.setLastName("Ramaswamy");
		teacher1.setPhoneNumber("9876543210");
		entityManager.persist(teacher1);
		
		Teacher teacher2 = new Teacher();
		teacher2.setFirstName("Madhav");
		teacher2.setLastName("Bhatki");
		teacher2.setPhoneNumber("1234567890");
		entityManager.persist(teacher2);
		
		entityManager.flush();
	}
	
	@Test
	public void teacherSkillAssociationShouldBeBidirectional() {
		// Given a teacher and a skill
		//Teacher teacher1 = teacherRepository.findOne((long) 1);
		//Skill skill1 = skillRepository.findOne((long) 1);
		
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").get(0);
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		
		// When I associate the skill with the teacher
		Set<Skill> skills = new HashSet<>();
		skills.add(skill1);
		teacher1.setSkills(skills);
		
		// And I associate the teacher with the skill
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher1);
		skill1.setTeachers(teachers);
		
		Teacher foundTeacher1 = teacherRepository.save(teacher1);
		
		// Then the teacher should be associated with the skill
		assertThat(foundTeacher1.getSkills()).contains(skill1);
		
		// And the skill should be associated with the teacher
		Skill foundSkill1 = skillRepository.findByName("Keyboard").get(0);
		assertThat(foundSkill1.getTeachers()).contains(teacher1);
	}
	
	@Test
	public void shouldBeAbleToAssociateMultipleSkillsWithSameTeacher() {
		// Given a teacher and 2 skills
		Teacher teacher2 = teacherRepository.findByFirstName("Madhav").get(0);
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		Skill skill2 = skillRepository.findByName("Guitar").get(0);
		
		// When I associate both the skills with the teacher
		Set<Skill> skills = new HashSet<>();
		skills.add(skill1);
		skills.add(skill2);
		teacher2.setSkills(skills);
		
		// And I associate the teacher with both the skills
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher2);
		skill1.setTeachers(teachers);
		skill2.setTeachers(teachers);
		
		Teacher foundTeacher2 = teacherRepository.save(teacher2);
		
		// Then the teacher should be associated with both the skills
		assertThat(foundTeacher2.getSkills()).contains(skill1, skill2);
	}
	
	@Test
	public void shouldBeAbleToAssociateOneSkillWithMultipleTeachers() {
		// Given a skill and 2 teachers
		Skill skill2 = skillRepository.findByName("Guitar").get(0);
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").get(0);
		Teacher teacher2 = teacherRepository.findByFirstName("Madhav").get(0);
		
		// When I associate the skill with both teachers
		Set<Skill> skills = new HashSet<>();
		skills.add(skill2);
		teacher1.setSkills(skills);
		teacher2.setSkills(skills);
		
		// And I associate both teachers with the skill
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher1);
		teachers.add(teacher2);
		skill2.setTeachers(teachers);
		
		teacherRepository.save(teacher1);
		teacherRepository.save(teacher2);
		
		// Then the skill should be associated with both the teachers
		Skill foundSkill2 = skillRepository.findByName("Guitar").get(0);
		assertThat(foundSkill2.getTeachers()).contains(teacher1, teacher2);
	}
	
	@Test
	public void shouldBeAbleToDisassociateSkillFromTeacher() {
		//Given a teacher with one associated skill
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").get(0);
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		
		Set<Skill> skills = new HashSet<>();
		skills.add(skill1);
		teacher1.setSkills(skills);
		
		Set<Teacher> teachers = new HashSet<>();
		teachers.add(teacher1);
		skill1.setTeachers(teachers);
		
		Teacher foundTeacher1 = teacherRepository.save(teacher1);
		assertThat(foundTeacher1.getSkills()).contains(skill1);
		
		// When I delete the association
		teacher1.setSkills(null);
		skill1.setTeachers(null);
		Teacher foundAgainTeacher1 = teacherRepository.save(teacher1);
		
		// Then the teacher should have no associated skill
		assertThat(foundAgainTeacher1.getSkills()).isNull();
	}
}