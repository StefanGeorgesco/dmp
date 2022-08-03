package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.List;

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

	private String recipientDoctorFirstname;
	
	private String recipientDoctorLastname;
	
	private List<String> recipientDoctorSpecialties;
	
}
