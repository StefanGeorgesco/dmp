package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un symptôme observé par un
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class SymptomDTO extends PatientFileItemDTO {

	/**
	 * Description du symptôme.
	 */
	@NotBlank(message = "La description est obligatoire.")
	private String description;

}
