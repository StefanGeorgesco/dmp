package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotNull;

public class PatientFile extends File {

	@NotNull(message = "referring doctor is mandatory")
	private Doctor referringDoctor;

	public Doctor getReferringDoctor() {
		return referringDoctor;
	}

	public void setReferringDoctor(Doctor referringDoctor) {
		this.referringDoctor = referringDoctor;
	}

}
