package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Act extends PatientFileItem {
	
	@NotNull(message = "medical act is mandatory")
	private MedicalAct medicalAct;

}
