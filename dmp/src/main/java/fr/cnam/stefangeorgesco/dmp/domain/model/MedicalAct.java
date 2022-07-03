package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalAct {
	
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "description is mandatory")
	private String description;

}
