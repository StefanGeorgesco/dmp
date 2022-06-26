package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Mail extends PatientFileItem {

	@NotBlank(message = "mail text is mandatory")
	String text;

	@NotNull(message = "recipient doctor is mandatory")
	private Doctor recipientDoctor;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Doctor getRecipientDoctor() {
		return recipientDoctor;
	}

	public void setRecipientDoctor(Doctor recipientDoctor) {
		this.recipientDoctor = recipientDoctor;
	}

}
