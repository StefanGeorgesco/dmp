package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
	
	@NotBlank(message = "invalid street1")
	private String street1;
	
	@NotNull(message = "street2 should not be null")
	private String street2 = "";
	
	@NotBlank(message = "invalid city")
	private String city;
	
	@NotNull(message = "state should not be null")
	private String state = "";
	
	@NotBlank(message = "invalid zipcode")
	private String zipcode;
	
	@NotBlank(message = "invalid country")
	private String country;

}
