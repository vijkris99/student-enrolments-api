package com.autopia.data.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Teacher;

@RepositoryRestResource
public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {
	
	List<Teacher> findByFirstName(@Param("firstName") String firstName);
	
	List<Teacher> findByLastName(@Param("lastName") String lastName);
	
	List<Teacher> findByFirstNameAndLastName(@Param("firstName") String firstName,
												@Param("lastName") String lastName);
	
	List<Teacher> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	Set<Teacher> findBySkills(@Param("skill") Skill skill);
	
	Set<Teacher> findBySkillsName(@Param("skillName") String skillName);
	
	Set<Teacher> findBySkillsIn(@Param("skills") Set<Skill> skills);
}