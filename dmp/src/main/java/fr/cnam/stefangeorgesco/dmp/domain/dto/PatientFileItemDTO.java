package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = ActDTO.class, name = "act"),
		@JsonSubTypes.Type(value = DiagnosisDTO.class, name = "diagnosis"),
		@JsonSubTypes.Type(value = MailDTO.class, name = "mail"),
		@JsonSubTypes.Type(value = PrescriptionDTO.class, name = "prescription"),
		@JsonSubTypes.Type(value = SymptomDTO.class, name = "symptom") })
public abstract class PatientFileItemDTO {

	private UUID id;

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
