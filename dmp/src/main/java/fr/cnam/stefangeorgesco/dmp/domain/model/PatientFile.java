package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

	@ManyToOne
	@JoinColumn(name = "referring_doctor_id")
	@NotNull(message = "referring doctor is mandatory")
	private Doctor referringDoctor;

}
