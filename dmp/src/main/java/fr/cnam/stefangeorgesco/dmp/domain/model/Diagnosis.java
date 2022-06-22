package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Diagnosis extends PatientFileItem {
	
	@NotNull(message = "disease is mandatory")
	@Valid
	private Disease disease;

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

}
