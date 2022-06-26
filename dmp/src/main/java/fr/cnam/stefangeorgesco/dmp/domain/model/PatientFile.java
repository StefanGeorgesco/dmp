package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "t_patient_file")
public class PatientFile extends File {

	@ManyToOne
	@JoinColumn(name = "referring_doctor_id")
	@NotNull(message = "referring doctor is mandatory")
	private Doctor referringDoctor;

	public Doctor getReferringDoctor() {
		return referringDoctor;
	}

	public void setReferringDoctor(Doctor referringDoctor) {
		this.referringDoctor = referringDoctor;
	}

}
