package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO extends PatientFileItemDTO {

	@NotBlank(message = "mail text is mandatory")
	String text;

	@NotBlank(message = "recipient doctor id is mandatory")
	private String recipientDoctorId;

}
