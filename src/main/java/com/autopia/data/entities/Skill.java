package com.autopia.data.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(
	name="Skills",
	uniqueConstraints=@UniqueConstraint(columnNames={"skillName"})
)
@Entity
@Data
@NoArgsConstructor
public class Skill {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(value=AccessLevel.NONE)
	private Long id;
	
	@Column(nullable=false)
	private String skillName;
	
	@ManyToMany(mappedBy="skills")
	private List<Teacher> teachers;
}