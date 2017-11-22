package com.autopia.data.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Skill;
import com.autopia.data.entities.Student;
import com.autopia.data.entities.Teacher;

public interface EnrolmentRepository extends PagingAndSortingRepository<Enrolment, Long> {
	
	List<Enrolment> findByTeacher(@Param("teacher") Teacher teacher);
	
	List<Enrolment> findByStudent(@Param("student") Student student);
	
	List<Enrolment> findBySkill(@Param("skill") Skill skill);
	
	List<Enrolment> findByIsActive(@Param("isActive") Boolean isActive);
}