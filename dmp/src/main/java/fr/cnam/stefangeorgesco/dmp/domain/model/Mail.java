package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Mail extends PatientFileItem {

	@NotBlank(message = "mail text is mandatory")
	String text;

	@NotNull(message = "recipient doctor is mandatory")
	@Valid
	private Doctor to;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Doctor getTo() {
		return to;
	}

	public void setTo(Doctor to) {
		this.to = to;
	}

}
