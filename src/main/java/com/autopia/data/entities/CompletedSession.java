package com.autopia.data.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(
	name="Sessions",
	uniqueConstraints=@UniqueConstraint(columnNames={"startTime"})
)
@Entity
@Data
@NoArgsConstructor
public class CompletedSession {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="enrolment_id", nullable=false)
	private Enrolment enrolment;
	
	@JsonFormat(timezone="UTC")
	@Column(nullable=false)
	private ZonedDateTime startTime;
	
	@JsonFormat(timezone="UTC")
	@Column(nullable=false)
	private ZonedDateTime endTime;
	
	@Column(nullable=false)
	private Integer feePaid;
	
	@PrePersist
	public void prePersist() {
		int sessionBalanceDue = this.enrolment.getSessionFee() - this.feePaid;
		this.enrolment.incrementBalanceDue(sessionBalanceDue);
	}
}