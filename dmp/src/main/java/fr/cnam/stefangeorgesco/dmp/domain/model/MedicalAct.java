package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MedicalAct {
	
	@NotBlank(message = "id is mandatory")
	String id;
	
	@NotBlank(message = "description is mandatory")
	String description;

}
