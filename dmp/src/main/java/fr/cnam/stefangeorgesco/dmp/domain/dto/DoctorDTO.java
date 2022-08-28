package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un dossier de médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class DoctorDTO extends FileDTO {

	/**
	 * Spécialités (objets de transfert de données) du médecin.
	 */
	@NotNull(message = "Les spécialités sont obligatoires.")
	@Size(min = 1, message = "Le médecin doit avoir au moins une spécialité.")
	@JsonProperty("specialties")
	private Collection<@Valid SpecialtyDTO> specialtiesDTO;

}
