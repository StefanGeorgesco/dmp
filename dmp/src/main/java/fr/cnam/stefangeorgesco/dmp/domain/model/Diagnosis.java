package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diagnosis extends PatientFileItem {
	
	@NotNull(message = "disease is mandatory")
	private Disease disease;

}
