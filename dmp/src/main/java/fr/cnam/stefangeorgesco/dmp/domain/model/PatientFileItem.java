package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PatientFileItem {

	private String id;

	@NotNull(message = "patient file date is mandatory")
	@PastOrPresent(message = "patient file item date must be in the past or today")
	private LocalDate date;
	
	private String comments;
	
	@NotNull(message = "authoring doctor is mandatory")
	private Doctor authoringDoctor;
	
	@NotNull(message = "patient file is mandatory")
	private PatientFile patientFile;

}
