package com.autopia.data.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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
	name="Sessions",
	uniqueConstraints=@UniqueConstraint(columnNames={"date", "time"})
)
@Entity
@Data
@NoArgsConstructor
public class Session {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="enrolment_id", nullable=false)
	private Enrolment enrolment;
	
	@Column(nullable=false)
	private LocalDate date;
	
	@Column(nullable=false)
	private LocalTime time;
	
	@Column(nullable=false)
	private Duration duration;
	
	private Boolean sessionConducted = false;
	private Integer feePaid = 0;
	
	private String cancelledBy = "N/A";
	private String cancelledReason = "N/A";
}