package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymptomDTO extends PatientFileItemDTO {

	@NotBlank(message = "symptom description is mandatory")
	private String description;

}
