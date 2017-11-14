package com.autopia.data.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.autopia.data.entities.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
	
}