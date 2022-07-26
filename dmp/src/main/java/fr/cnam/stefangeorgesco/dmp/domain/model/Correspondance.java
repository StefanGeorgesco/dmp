package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_correspondance")
@Getter
@Setter
public class Correspondance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message = "correspondance date is mandatory")
	@Future(message = "correpondance date must be in the future")
	private LocalDate dateUntil;

	@ManyToOne
	@JoinColumn(name = "doctor_id")
	@NotNull(message = "doctor is mandatory")
	private Doctor doctor;

	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "patient file is mandatory")
	private PatientFile patientFile;

}
