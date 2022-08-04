package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalActDTO {

	@NotBlank(message = "id is mandatory")
	private String id;
	
	private String description;

}
