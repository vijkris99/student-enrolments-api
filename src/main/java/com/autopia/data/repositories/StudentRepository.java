package com.autopia.data.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.autopia.data.entities.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
	
	List<Student> findByFirstName(@Param("firstName") String firstName);
	
	List<Student> findByLastName(@Param("lastName") String lastName);
	
	List<Student> findByFirstNameAndLastName(@Param("firstName") String firstName,
												@Param("lastName") String lastName);
	
	List<Student> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}