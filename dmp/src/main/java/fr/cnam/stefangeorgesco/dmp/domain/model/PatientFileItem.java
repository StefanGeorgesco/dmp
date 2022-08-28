package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe abstraite parente des entités représentant les éléments médicaux des
 * dossiers patients.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_patient_file_item")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class PatientFileItem {

	/**
	 * Identifiant
	 */
	@Id
	@GeneratedValue
	@Type(type = "org.hibernate.type.UUIDCharType")
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
	 * Médecin auteur de l'élément médical.
	 */
	@ManyToOne
	@JoinColumn(name = "authoring_doctor_id")
	@NotNull(message = "Le médecin auteur est obligatoire.")
	private Doctor authoringDoctor;

	/**
	 * Dossier patient auquel l'élément médical est associé.
	 */
	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "Le dossier patient est obligatoire.")
	private PatientFile patientFile;

}
