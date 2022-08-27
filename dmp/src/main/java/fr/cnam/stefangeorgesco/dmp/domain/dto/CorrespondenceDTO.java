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
public class CorrespondenceDTO {
	
	private UUID id;

	@NotNull(message = "La date de la correspondance est obligatoire.")
	@Future(message = "La date de la correspondance doit être dans le futur.")
	private LocalDate dateUntil;

	@NotBlank(message = "L'identifiant du médecin est obligatoire.")
	private String doctorId;
	
	private String doctorFirstname;

	private String doctorLastname;
	
	private List<String> doctorSpecialties;

	private String patientFileId;

}
