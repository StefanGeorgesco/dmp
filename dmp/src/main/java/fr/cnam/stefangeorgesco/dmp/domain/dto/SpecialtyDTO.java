package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant une spécialité médicale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class SpecialtyDTO {

	/**
	 * Identifiant.
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Libellé.
	 */
	private String description;

}
