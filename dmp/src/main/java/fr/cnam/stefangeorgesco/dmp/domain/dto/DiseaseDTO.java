package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant une maladie de la nomenclature CIM
 * 10.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class DiseaseDTO {

	/**
	 * Identifiant, champ 'Code du diagnostic' de la nomenclature CIM 10
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Champ 'Libellé long' de la nomenclature CIM 10
	 */
	private String description;

}
