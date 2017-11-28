package com.autopia.data.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.Enrolment;
import com.autopia.data.entities.Session;

public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {
	
	List<Session> findByEnrolment(@Param("enrolment") Enrolment enrolment);
	
	Session findByStartTime(@Param("startTime") ZonedDateTime startTime);
	
	Session findByStartTimeAndEndTime(@Param("startTime") ZonedDateTime startTime,
										@Param("startTime") ZonedDateTime endTime);
	
	List<Session> findByCompleted(@Param("completed") Boolean completed);
	
	Long countByCompleted(@Param("completed") Boolean completed);
}