package com.autopia.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.autopia.data.entities.Enrolment;

public interface EnrolmentRepository extends PagingAndSortingRepository<Enrolment, Long> {
	
}