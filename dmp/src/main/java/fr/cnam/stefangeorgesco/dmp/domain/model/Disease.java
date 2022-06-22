package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Disease {
	
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "description is mandatory")
	private String description;

}
