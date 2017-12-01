package com.autopia.data.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.Enrolment;

public interface EnrolmentRepository extends PagingAndSortingRepository<Enrolment, Long> {
	
	List<Enrolment> findByTeacherId(@Param("teacherId") long teacherId);
	
	List<Enrolment> findByTeacherFirstName(@Param("teacherFirstName") String teacherFirstName);
	
	List<Enrolment> findByStudentId(@Param("studentId") long studentId);
	
	List<Enrolment> findByStudentFirstName(@Param("studentFirstName") String studentFirstName);
	
	List<Enrolment> findBySkillId(@Param("skillId") long skillId);
	
	List<Enrolment> findBySkillName(@Param("skillName") String skillName);
	
	List<Enrolment> findByBalanceDueGreaterThan(@Param("balanceDueGreaterThan") Integer balanceDueGreaterThan);
	
	List<Enrolment> findByIsActive(@Param("isActive") Boolean isActive);
	
	List<Enrolment> findByStartDateAfter(@Param("startDateAfter") ZonedDateTime startDateAfter);
	
	List<Enrolment> findByStartDateBefore(@Param("startDateBefore") ZonedDateTime startDateBefore);
}