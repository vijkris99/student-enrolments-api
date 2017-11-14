package com.autopia.data.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="skill_id")
	private Skill skill;
	
	@OneToMany(mappedBy="enrolment")
	private List<Session> sessions;
	
	private @NonNull Integer sessionFee;
	private Integer balanceDue;
	private Boolean isActive;
	
	private @NonNull LocalDate startDate;
	private LocalDate endDate;
}