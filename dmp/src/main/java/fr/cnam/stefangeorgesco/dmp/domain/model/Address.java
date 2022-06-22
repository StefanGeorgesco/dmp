package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Address {

	@NotBlank(message = "invalid street")
	String street1;
	
	String street2;
	
	@NotBlank(message = "invalid city")
	String city;
	
	String state;
	
	@NotBlank(message = "invalid zipcode")
	String zipcode;
	
	@NotBlank(message = "invalid country")
	String country;

}
