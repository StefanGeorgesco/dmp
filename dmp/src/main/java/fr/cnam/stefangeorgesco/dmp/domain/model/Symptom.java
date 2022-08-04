package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_symptom")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Symptom extends PatientFileItem {
	
	@Column(length = 800)
	@NotBlank(message = "symptom description is mandatory")
	private String description;

}
