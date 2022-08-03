package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PatientFileItemDTO {

	private long id;

	@NotNull(message = "patient file date is mandatory")
	@PastOrPresent(message = "patient file item date must be in the past or today")
	private LocalDate date;
	
	private String comments;
	
	@NotBlank(message = "authoring doctor id is mandatory")
	private String authoringDoctorId;
	
	private String authoringDoctorFirstname;
	
	private String authoringDoctorLastname;
	
	private List<String> authoringDoctorSpecialties;
	
	@NotBlank(message = "patient file id is mandatory")
	private String patientFileId;

}
