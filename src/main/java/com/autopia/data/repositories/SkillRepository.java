package com.autopia.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.autopia.data.entities.Skill;

public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {
	
}