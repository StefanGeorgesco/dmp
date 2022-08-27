package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO extends PatientFileItemDTO {

	@NotBlank(message = "Le texte du courrier est obligatoire.")
	String text;

	@NotBlank(message = "L'identifiant du médecin destinataire est obligatoire.")
	private String recipientDoctorId;

	private String recipientDoctorFirstname;
	
	private String recipientDoctorLastname;
	
	private List<String> recipientDoctorSpecialties;
	
}
