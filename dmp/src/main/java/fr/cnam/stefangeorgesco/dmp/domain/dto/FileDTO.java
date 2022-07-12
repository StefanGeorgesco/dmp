package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
	
	@NotBlank(message = "id is mandatory")
	protected String id;

	@NotBlank(message = "firstname is mandatory")
	protected String firstname;

	@NotBlank(message = "lastname is mandatory")
	protected String lastname;

	@NotBlank(message = "phone is mandatory")
	protected String phone;

	@NotBlank(message = "email is mandatory")
	@Email(message = "email must be given and respect format")
	protected String email;

	@NotNull(message = "address is mandatory")
	@Valid
	protected AddressDTO addressDTO;

	protected String securityCode;

}
