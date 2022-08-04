package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisDTO extends PatientFileItemDTO {

	@NotNull(message = "disease is mandatory")
	@Valid
	@JsonProperty("disease")
	private DiseaseDTO diseaseDTO;

}
