package com.autopia.data.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Table(name="Students")
@Entity
@Data
@NoArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	private @NonNull String firstName;
	private String lastName;
	private String phoneNumber;
	
	@OneToMany(mappedBy="student")
	private List<Enrolment> enrolments;
}