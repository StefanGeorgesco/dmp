package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "t_specialty")
@Data
public class Specialty {
	
	@Id
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "description is mandatory")
	private String description;

}
