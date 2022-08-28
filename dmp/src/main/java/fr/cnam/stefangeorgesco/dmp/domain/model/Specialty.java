package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant une spécialité médicale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_specialty")
@Getter
@Setter
public class Specialty {
	
	/**
	 * Identifiant.
	 */
	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	/**
	 * Libellé.
	 */
	@NotBlank(message = "La description est obligatoire.")
	private String description;
	
	/**
	 * Représente la spécialité sous forme {@link java.lang.String}
	 */
	@Override
	public String toString() {
		return this.description;
	}

}
