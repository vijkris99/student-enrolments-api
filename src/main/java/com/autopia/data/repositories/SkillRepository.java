package com.autopia.data.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.Skill;

public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {
	
	List<Skill> findBySkillName(@Param("skillName") String skillName);
}