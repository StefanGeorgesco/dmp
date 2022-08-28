package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant une prescription délivrée par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class PrescriptionDTO extends PatientFileItemDTO {

	/**
	 * Contenu de la prescription.
	 */
	@NotBlank(message = "La description est obligatoire.")
	private String description;

}
