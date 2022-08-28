package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant un dossier patient.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class PatientFileDTO extends FileDTO {

	/**
	 * Date de naissance du patient.
	 */
	@NotNull(message = "La date de naissance est obligatoire.")
	@PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dateOfBirth;

	/**
	 * Identifiant du médecin référent.
	 */
	private String referringDoctorId;

	/**
	 * Prénom du médecin référent.
	 */
	private String referringDoctorFirstname;

	/**
	 * Nom du médecin référent.
	 */
	private String referringDoctorLastname;

	/**
	 * Spécialités médicales du médecin référent (descriptions seulement).
	 */
	private List<String> referringDoctorSpecialties;
}
