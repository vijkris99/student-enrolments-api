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

@Table(name="Teachers")
@Entity
public class Teacher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String firstName;
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
	
	
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<Enrolment> getEnrolments() {
		return enrolments;
	}

	public void setEnrolments(List<Enrolment> enrolments) {
		this.enrolments = enrolments;
	}
}