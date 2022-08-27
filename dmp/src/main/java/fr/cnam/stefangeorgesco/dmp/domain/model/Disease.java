package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_disease")
@Getter
@Setter
public class Disease {
	
	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	@Column(length = 320)
	@NotBlank(message = "La description est obligatoire.")
	private String description;

}
