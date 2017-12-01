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
	public void shouldBeAbleToAssociateMultipleSkillsWithSameTeacher() {
		// Given a teacher and 2 skills
		Teacher teacher2 = teacherRepository.findByFirstName("Madhav").iterator().next();
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		Skill skill2 = skillRepository.findByName("Guitar").get(0);
		
		// When I associate both the skills with the teacher
		teacher2.addSkill(skill1);
		teacher2.addSkill(skill2);
		Teacher savedTeacher2 = teacherRepository.save(teacher2);
		
		// Then the teacher should be associated with both the skills
		assertThat(savedTeacher2.getSkills().size()).isEqualTo(2);
		assertThat(savedTeacher2.getSkills()).contains(skill1, skill2);
		
		// And the teacher should be searchable by the first skill
		Set<Teacher> foundTeachersBySkill1 = teacherRepository.findBySkillsId(skill1.getId());
		assertThat(foundTeachersBySkill1.size()).isEqualTo(1);
		Teacher foundTeacher2BySkill1 = foundTeachersBySkill1.iterator().next();
		assertThat(foundTeacher2BySkill1.getSkills().size()).isEqualTo(2);
		assertThat(foundTeacher2BySkill1.getSkills()).contains(skill1, skill2);
		
		// And the teacher should be searchable by the second skill
		Set<Teacher> foundTeachersBySkill2 = teacherRepository.findBySkillsId(skill2.getId());
		assertThat(foundTeachersBySkill2.size()).isEqualTo(1);
		Teacher foundTeacher2BySkill2 = foundTeachersBySkill2.iterator().next();
		assertThat(foundTeacher2BySkill2.getSkills().size()).isEqualTo(2);
		assertThat(foundTeacher2BySkill2.getSkills()).contains(skill1, skill2);
		
		// And the teacher should be searchable by both skills together
		Set<Long> skillIds = new HashSet<>();
		skillIds.add(skill1.getId());
		skillIds.add(skill2.getId());
		Set<Teacher> foundTeachersByBothSkills = teacherRepository.findBySkillsIdIn(skillIds);
		assertThat(foundTeachersByBothSkills.size()).isEqualTo(1);
		Teacher foundTeacher2ByBothSkills = foundTeachersByBothSkills.iterator().next();
		assertThat(foundTeacher2ByBothSkills.getSkills().size()).isEqualTo(2);
		assertThat(foundTeacher2ByBothSkills.getSkills()).contains(skill1, skill2);
	}
	
	@Test
	public void shouldBeAbleToAssociateOneSkillWithMultipleTeachers() {
		// Given a skill and 2 teachers
		Skill skill2 = skillRepository.findByName("Guitar").get(0);
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").iterator().next();
		Teacher teacher2 = teacherRepository.findByFirstName("Madhav").iterator().next();
		
		// When I associate the skill with the first teacher
		teacher1.addSkill(skill2);
		Teacher savedTeacher1 = teacherRepository.save(teacher1);
		
		// And I associate the skill with the second teacher
		teacher2.addSkill(skill2);
		Teacher savedTeacher2 = teacherRepository.save(teacher2);
		
		// Then the skill should be associated with the first teacher
		assertThat(savedTeacher1.getSkills().size()).isEqualTo(1);
		assertThat(savedTeacher1.getSkills()).contains(skill2);
		
		// And the skill should be associated with the second teacher
		assertThat(savedTeacher2.getSkills().size()).isEqualTo(1);
		assertThat(savedTeacher2.getSkills()).contains(skill2);
		
		// And both teachers should be searchable by the skill
		Set<Teacher> foundTeachersBySkill2 = teacherRepository.findBySkillsId(skill2.getId());
		assertThat(foundTeachersBySkill2.size()).isEqualTo(2);
		for (Teacher foundTeacherBySkill2 : foundTeachersBySkill2) {
			assertThat(foundTeacherBySkill2.getSkills()).contains(skill2);
		}
	}
	
	@Test
	public void findBySkillsNameShouldWork() {
		// Given a teacher and a skill
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").iterator().next();
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		
		// When I associate the skill with the teacher
		teacher1.addSkill(skill1);
		Teacher savedTeacher1 = teacherRepository.save(teacher1);
		
		// Then the skill should be associated with the teacher
		assertThat(savedTeacher1.getSkills().size()).isEqualTo(1);
		assertThat(savedTeacher1.getSkills()).contains(skill1);
		
		// And the teacher should be searchable by the skill name
		Set<Teacher> foundTeachersBySkill1Name = teacherRepository.findBySkillsName(skill1.getName());
		assertThat(foundTeachersBySkill1Name.size()).isEqualTo(1);
		Teacher foundTeacher1BySkill1 = foundTeachersBySkill1Name.iterator().next();
		assertThat(foundTeacher1BySkill1.getSkills().size()).isEqualTo(1);
		assertThat(foundTeacher1BySkill1.getSkills()).contains(skill1);
	}
	
	@Test
	public void findBySkillsIdShouldWork() {
		// Given a teacher and a skill
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").iterator().next();
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		
		// When I associate the skill with the teacher
		teacher1.addSkill(skill1);
		Teacher savedTeacher1 = teacherRepository.save(teacher1);
		
		// Then the skill should be associated with the teacher
		assertThat(savedTeacher1.getSkills().size()).isEqualTo(1);
		assertThat(savedTeacher1.getSkills()).contains(skill1);
		
		// And the teacher should be searchable by the skill name
		Set<Teacher> foundTeachersBySkill1Id = teacherRepository.findBySkillsId(skill1.getId());
		assertThat(foundTeachersBySkill1Id.size()).isEqualTo(1);
		Teacher foundTeacher1BySkill1 = foundTeachersBySkill1Id.iterator().next();
		assertThat(foundTeacher1BySkill1.getSkills().size()).isEqualTo(1);
		assertThat(foundTeacher1BySkill1.getSkills()).contains(skill1);
	}
	
	@Test
	public void shouldBeAbleToDisassociateSkillFromTeacher() {
		//Given a teacher with one associated skill
		Teacher teacher1 = teacherRepository.findByFirstName("Vijay").iterator().next();
		Skill skill1 = skillRepository.findByName("Keyboard").get(0);
		teacher1.addSkill(skill1);
		Teacher savedTeacher1 = entityManager.persist(teacher1);
		assertThat(savedTeacher1.getSkills().size()).isEqualTo(1);
		assertThat(savedTeacher1.getSkills()).contains(skill1);
		
		// When I delete the association
		teacher1.removeSkill(skill1);
		Teacher savedAgainTeacher1 = teacherRepository.save(teacher1);
		
		// Then the teacher should have no associated skill
		assertThat(savedAgainTeacher1.getSkills().size()).isEqualTo(0);
		assertThat(savedAgainTeacher1.getSkills()).doesNotContain(skill1);
		
		// And the skill should be unaffected by the disassociation
		Skill foundAgainskill1 = skillRepository.findByName("Keyboard").get(0);
		assertThat(foundAgainskill1).isNotNull();
		assertThat(foundAgainskill1.getName()).isEqualTo("Keyboard");
	}
}