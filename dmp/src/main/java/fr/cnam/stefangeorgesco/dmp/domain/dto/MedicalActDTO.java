package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalActDTO {

	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "description is mandatory")
	private String description;

}
