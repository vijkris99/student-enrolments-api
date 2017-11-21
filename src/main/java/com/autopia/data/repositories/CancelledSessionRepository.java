package com.autopia.data.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.CancelledSession;
import com.autopia.data.entities.Enrolment;

public interface CancelledSessionRepository extends PagingAndSortingRepository<CancelledSession, Long> {
	
	List<CancelledSession> findByEnrolment(@Param("enrolment") Enrolment enrolment);
}