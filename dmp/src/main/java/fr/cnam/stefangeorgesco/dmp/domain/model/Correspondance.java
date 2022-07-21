package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Correspondance {

	private long id;

	@NotNull(message = "correspondance date is mandatory")
	@Future(message = "correpondance date must be in the future")
	private LocalDate dateUntil;

	@NotNull(message = "doctor is mandatory")
	Doctor doctor;

	@NotNull(message = "patient file is mandatory")
	PatientFile patientFile;

}
