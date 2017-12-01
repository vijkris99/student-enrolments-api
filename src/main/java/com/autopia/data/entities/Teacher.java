package com.autopia.data.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="Teachers")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Teacher {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	private String phoneNumber;
	
	@ManyToMany(
		cascade= {CascadeType.MERGE, CascadeType.PERSIST},
		fetch=FetchType.LAZY
	)
	@JoinTable(
		name="Teacher_Skills",
		joinColumns=@JoinColumn(name="teacher_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="skill_id", referencedColumnName="id")
	)
	@Setter(value=AccessLevel.NONE)
	private Set<Skill> skills = new HashSet<>();
	
	public void addSkill(Skill skill) {
		this.skills.add(skill);
	}
	
	public void removeSkill(Skill skill) {
		this.skills.remove(skill);
	}
}