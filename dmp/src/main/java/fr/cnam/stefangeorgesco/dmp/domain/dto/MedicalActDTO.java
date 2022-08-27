package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalActDTO {

	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	private String description;

}
