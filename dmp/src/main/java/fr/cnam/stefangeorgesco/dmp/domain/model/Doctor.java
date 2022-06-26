package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "t_doctor")
public class Doctor extends File {

	@ManyToMany
	@JoinTable(name = "t_doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	@NotNull(message = "specialties are mandatory")
	@Size(min = 1, message = "doctor must have at least one specialty")
	private Collection<Specialty> specialties;

	public Collection<Specialty> getSpecialties() {
		return specialties;
	}

	public void setSpecialties(Collection<Specialty> specialties) {
		this.specialties = specialties;
	}

}
