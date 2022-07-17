package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_doctor")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Doctor extends File {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	@NotNull(message = "specialties are mandatory")
	@Size(min = 1, message = "doctor must have at least one specialty")
	private Collection<Specialty> specialties;

}
