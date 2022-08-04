package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActDTO extends PatientFileItemDTO {

	@NotNull(message = "medical act is mandatory")
	@Valid
	@JsonProperty("medicalAct")
	private MedicalActDTO medicalActDTO;

}
