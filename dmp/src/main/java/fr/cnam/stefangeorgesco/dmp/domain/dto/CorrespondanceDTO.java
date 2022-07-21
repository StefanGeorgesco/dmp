package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrespondanceDTO {
	
	private long id;

	@NotNull(message = "correspondance date is mandatory")
	@Future(message = "correpondance date must be in the future")
	private LocalDate dateUntil;

	@NotBlank(message = "doctor is mandatory")
	String doctorId;

	@NotBlank(message = "patient file is mandatory")
	String patientFileId;

}
