package com.autopia.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.autopia.data.entities.Session;

public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {
	
}