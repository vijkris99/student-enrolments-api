package com.autopia.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.autopia.data.entities.Teacher;

@RepositoryRestResource
public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {
	
}