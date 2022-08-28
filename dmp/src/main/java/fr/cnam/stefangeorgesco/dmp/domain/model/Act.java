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
 * Entité représentant un acte dispensé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_act")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Act extends PatientFileItem {

	/**
	 * Acte médical de la nomenclature correspondant à l'acte dispensé.
	 */
	@ManyToOne
	@JoinColumn(name = "medical_act_id")
	@NotNull(message = "L'acte médical est obligatoire.")
	private MedicalAct medicalAct;

}
