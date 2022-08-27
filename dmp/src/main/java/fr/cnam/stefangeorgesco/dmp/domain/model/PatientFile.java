package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_patient_file")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class PatientFile extends File {

	@NotNull(message = "La date de naissance est obligatoire.")
	@PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@ManyToOne
	@JoinColumn(name = "referring_doctor_id")
	@NotNull(message = "Le médecin référent est obligatoire.")
	private Doctor referringDoctor;

}
