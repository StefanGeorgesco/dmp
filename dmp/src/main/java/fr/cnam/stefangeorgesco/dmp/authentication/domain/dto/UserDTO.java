package fr.cnam.stefangeorgesco.dmp.authentication.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@NotBlank(message = "password is mandatory")
	@Size(min=4, message = "password should at least be 4 characters long")
	private String password;
	
	private String securityCode;
	
}
