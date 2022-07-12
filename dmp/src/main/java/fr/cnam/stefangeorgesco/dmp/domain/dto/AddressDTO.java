package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
	
	@NotBlank(message = "invalid street")
	private String street1;
	
	private String street2;
	
	@NotBlank(message = "invalid city")
	private String city;
	
	private String state;
	
	@NotBlank(message = "invalid zipcode")
	private String zipcode;
	
	@NotBlank(message = "invalid country")
	private String country;

}
