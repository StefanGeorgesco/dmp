package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotNull;

public class Act extends PatientFileItem {
	
	@NotNull(message = "medical act is mandatory")
	private MedicalAct medicalAct;

	public MedicalAct getMedicalAct() {
		return medicalAct;
	}

	public void setMedicalAct(MedicalAct medicalAct) {
		this.medicalAct = medicalAct;
	}

}
