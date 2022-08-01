package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_specialty")
@Getter
@Setter
public class Specialty {
	
	@Id
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "description is mandatory")
	private String description;
	
	@Override
	public String toString() {
		return this.description;
	}

}
