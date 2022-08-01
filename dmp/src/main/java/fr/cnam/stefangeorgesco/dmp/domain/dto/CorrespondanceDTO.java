package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrespondanceDTO {
	
	private UUID id;

	@NotNull(message = "correspondance date is mandatory")
	@Future(message = "correpondance date must be in the future")
	private LocalDate dateUntil;

	@NotBlank(message = "doctor is mandatory")
	private String doctorId;
	
	private String doctorFirstName;

	private String doctorLastName;
	
	private List<String> doctorSpecialties;

	private String patientFileId;

}
