package com.autopia.data.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.CancelledSession;
import com.autopia.data.entities.Enrolment;

public interface CancelledSessionRepository extends PagingAndSortingRepository<CancelledSession, Long> {
	
	List<CancelledSession> findByEnrolment(@Param("enrolment") Enrolment enrolment);
	
	CancelledSession findByStartTime(@Param("startTime") ZonedDateTime startTime);
	
	CancelledSession findByStartAndEndTime(@Param("startTime") ZonedDateTime startTime,
											@Param("startTime") ZonedDateTime endTime);
	
	List<CancelledSession> findBySessionCompleted(@Param("sessionCompleted") Boolean sessionCompleted);
}