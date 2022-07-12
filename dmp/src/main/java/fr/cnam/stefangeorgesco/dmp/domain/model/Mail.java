package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail extends PatientFileItem {

	@NotBlank(message = "mail text is mandatory")
	String text;

	@NotNull(message = "recipient doctor is mandatory")
	private Doctor recipientDoctor;

}
