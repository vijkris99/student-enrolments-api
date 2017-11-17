package com.autopia.data.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(
	name="Enrolments",
	uniqueConstraints=@UniqueConstraint(columnNames={"teacher_id", "student_id", "skill_id"})
)
@Entity
@Data
@NoArgsConstructor
public class Enrolment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="teacher_id", nullable=false)
	private Teacher teacher;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable=false)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name="skill_id", nullable=false)
	private Skill skill;
	
	@Column(nullable=false)
	private Integer sessionFee;
	
	private Integer balanceDue = 0;
	private Boolean isActive = true;
	
	private LocalDate startDate;
	private LocalDate endDate;
}