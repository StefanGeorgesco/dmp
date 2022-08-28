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

/**
 * Entité représentant un diagnostic réalisé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_diagnosis")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Diagnosis extends PatientFileItem {

	/**
	 * Maladie de la nomenclature diagnostiquée.
	 */
	@ManyToOne
	@JoinColumn(name = "disease_id")
	@NotNull(message = "La maladie est obligatoire.")
	private Disease disease;

}
