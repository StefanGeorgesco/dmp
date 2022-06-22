package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

public class Prescription extends PatientFileItem {
	
	@NotBlank(message = "prescription description is mandatory")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
