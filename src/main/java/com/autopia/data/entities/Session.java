package com.autopia.data.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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
import lombok.NonNull;
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
	@JoinColumn(name="enrolment_id")
	private Enrolment enrolment;
	
	private @NonNull LocalDate date;
	private @NonNull LocalTime time;
	private @NonNull Duration duration;
	
	private Boolean sessionConducted;
	private @NonNull Integer feePaid;
	
	private String cancelledBy;
	private String cancelledReason;
}