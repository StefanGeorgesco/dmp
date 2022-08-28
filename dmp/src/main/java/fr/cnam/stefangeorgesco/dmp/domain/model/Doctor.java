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

/**
 * Entité représentant un dossier de médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_doctor")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Doctor extends File {

	/**
	 * Spécialités du médecin.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
	@NotNull(message = "Les spécialités sont obligatoires.")
	@Size(min = 1, message = "Le médecin doit avoir au moins une spécialité.")
	private Collection<Specialty> specialties;

}
