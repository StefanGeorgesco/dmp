package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un acte médical de la nomenclature
 * CCAM.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class MedicalActDTO {

	/**
	 * Identifiant, champ 'Code' de la nomenclature CCAM
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Texte' de la nomenclature CCAM
	 */
	private String description;

}
