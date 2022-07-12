package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prescription extends PatientFileItem {
	
	@NotBlank(message = "prescription description is mandatory")
	private String description;

}
