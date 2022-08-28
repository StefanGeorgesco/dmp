package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe abstraite parente des objets de transfert de données représentant les éléments
 * médicaux des dossiers patients.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = ActDTO.class, name = "act"),
		@JsonSubTypes.Type(value = DiagnosisDTO.class, name = "diagnosis"),
		@JsonSubTypes.Type(value = MailDTO.class, name = "mail"),
		@JsonSubTypes.Type(value = PrescriptionDTO.class, name = "prescription"),
		@JsonSubTypes.Type(value = SymptomDTO.class, name = "symptom") })
public abstract class PatientFileItemDTO {

	/**
	 * Identifiant
	 */
	private UUID id;

	/**
	 * Date de création de l'élément médical.
	 */
	@NotNull(message = "La date de l'élément de dossier patient est obligatoire.")
	@PastOrPresent(message = "La date de l'élément de dossier patient doit être dans le passé ou aujourd'hui.")
	private LocalDate date;

	/**
	 * Commentaires.
	 */
	private String comments;

	/**
	 * Identifiant du médecin auteur de l'élément médical.
	 */
	private String authoringDoctorId;

	/**
	 * Prénom du médecin auteur de l'élément médical.
	 */
	private String authoringDoctorFirstname;

	/**
	 * Nom du médecin auteur de l'élément médical.
	 */
	private String authoringDoctorLastname;

	/**
	 * Spécialités médicales du médecin auteur de l'élément médical (descriptions seulement).
	 */
	private List<String> authoringDoctorSpecialties;

	/**
	 * Identifiant du dossier patient auquel l'élément médical est associé.
	 */
	private String patientFileId;

}
