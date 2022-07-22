package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO extends PatientFileItemDTO {

	@NotBlank(message = "prescription description is mandatory")
	private String description;

}
