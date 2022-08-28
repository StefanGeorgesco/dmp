package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant un symptôme observé par un médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_symptom")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Symptom extends PatientFileItem {
	
	/**
	 * Description du symptôme.
	 */
	@Column(length = 800)
	@NotBlank(message = "La description est obligatoire.")
	private String description;

}
