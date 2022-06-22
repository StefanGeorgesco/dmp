package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Doctor extends File {

	@NotNull(message = "specialties are mandatory")
	@Size(min = 1, message = "doctor must have at least one specialty")
	private Collection<@Valid Specialty> specialties;

	public Collection<Specialty> getSpecialties() {
		return specialties;
	}

	public void setSpecialties(Collection<Specialty> specialties) {
		this.specialties = specialties;
	}

}
