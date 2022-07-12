package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Symptom extends PatientFileItem {
	
	@NotBlank(message = "symptom description is mandatory")
	private String description;

}
