package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

	@NotBlank(message = "invalid street")
	private String street1;
	
	@NotNull
	private String street2 = "";
	
	@NotBlank(message = "invalid city")
	private String city;
	
	@NotNull
	private String state = "";
	
	@NotBlank(message = "invalid zipcode")
	private String zipcode;
	
	@NotBlank(message = "invalid country")
	private String country;

}
