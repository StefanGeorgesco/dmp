package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un courrier adressé par le médecin
 * référent à un autre médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class MailDTO extends PatientFileItemDTO {

	/**
	 * Texte du courrier.
	 */
	@NotBlank(message = "Le texte du courrier est obligatoire.")
	String text;

	/**
	 * Identifiant du médecin destinataire.
	 */
	@NotBlank(message = "L'identifiant du médecin destinataire est obligatoire.")
	private String recipientDoctorId;

	/**
	 * Prénom du médecin destinataire.
	 */
	private String recipientDoctorFirstname;

	/**
	 * Nom du médecin destinataire.
	 */
	private String recipientDoctorLastname;

	/**
	 * Spécialités médicales du médecin destinataire (descriptions seulement).
	 */
	private List<String> recipientDoctorSpecialties;

}
