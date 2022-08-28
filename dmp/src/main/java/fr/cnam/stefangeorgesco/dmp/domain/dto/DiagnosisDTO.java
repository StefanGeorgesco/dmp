package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un diagnostic réalisé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class DiagnosisDTO extends PatientFileItemDTO {

	/**
	 * Maladie de la nomenclature diagnostiquée.
	 */
	@NotNull(message = "La maladie est obligatoire.")
	@Valid
	@JsonProperty("disease")
	private DiseaseDTO diseaseDTO;

}
