package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Embeddable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

	@NotBlank(message = "Champ 'street1' invalide.")
	private String street1;
	
	@NotNull(message = "Le champ 'street2' ne doit pas être 'null'.")
	private String street2 = "";
	
	@NotBlank(message = "Champ 'city' invalide.")
	private String city;
	
	@NotNull(message = "Le champ 'state' ne doit pas être 'null'.")
	private String state = "";
	
	@NotBlank(message = "Champ 'zipcode' invalide.")
	private String zipcode;
	
	@NotBlank(message = "Champ 'country' invalide.")
	private String country;

}
