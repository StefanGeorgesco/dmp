package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Act extends PatientFileItem {
	
	@NotNull(message = "medical act is mandatory")
	@Valid
	private MedicalAct medicalAct;

	public MedicalAct getMedicalAct() {
		return medicalAct;
	}

	public void setMedicalAct(MedicalAct medicalAct) {
		this.medicalAct = medicalAct;
	}

}
