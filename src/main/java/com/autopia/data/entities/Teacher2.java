package com.autopia.data.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Table(name="Teachers")
@Entity
@Data
@NoArgsConstructor
public class Teacher2 {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	private @NonNull String firstName;
	private String lastName;
	private String phoneNumber;
	
	@ManyToMany
	@JoinTable(
		name="Teacher_Skills",
		joinColumns=@JoinColumn(name="teacher_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="skill_id", referencedColumnName="id"),
		uniqueConstraints=@UniqueConstraint(columnNames={"teacher_id", "skill_id"})
	)
	private List<Skill> skills;
	
	@OneToMany(mappedBy="teacher")
	private List<Enrolment> enrolments;
}