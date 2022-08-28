package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant une correspondance avec un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_correspondence")
@Getter
@Setter
public class Correspondence {

	@Id
	@GeneratedValue
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;

	/**
	 * Date limite d'effet de la correspondance.
	 */
	@Column(name = "date_until")
	@NotNull(message = "La date de la correspondance est obligatoire.")
	@Future(message = "La date de la correspondance doit être dans le futur.")
	private LocalDate dateUntil;

	/**
	 * Médecin correspondant.
	 */
	@ManyToOne
	@JoinColumn(name = "doctor_id")
	@NotNull(message = "L'identifiant du médecin est obligatoire.")
	private Doctor doctor;

	/**
	 * Dossier patient auquel la correspondance est associée.
	 */
	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "Le dossier patient est obligatoire.")
	private PatientFile patientFile;

}
