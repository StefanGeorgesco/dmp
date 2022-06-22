package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

public class Mail extends PatientFileItem {
	
	@NotBlank(message = "mail text is mandatory")
	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
