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

@Entity
@Table(name = "t_patient_file_item")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class PatientFileItem {

	@Id
	@GeneratedValue
	@Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

	@NotNull(message = "patient file date is mandatory")
	@PastOrPresent(message = "patient file item date must be in the past or today")
	private LocalDate date;
	
	private String comments;
	
	@ManyToOne
	@JoinColumn(name = "authoring_doctor_id")
	@NotNull(message = "authoring doctor is mandatory")
	private Doctor authoringDoctor;
	
	@ManyToOne
	@JoinColumn(name = "patient_file_id")
	@NotNull(message = "patient file is mandatory")
	private PatientFile patientFile;

}
