package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_medical_act")
@Getter
@Setter
public class MedicalAct {
	
	@Id
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@Column(length = 800)
	@NotBlank(message = "description is mandatory")
	private String description;

}
