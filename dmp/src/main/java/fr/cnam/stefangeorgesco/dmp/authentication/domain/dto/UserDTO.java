package fr.cnam.stefangeorgesco.dmp.authentication.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserDTO {

	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	private String role;
	
	@NotBlank(message = "password is mandatory")
	@Size(min=4, message = "password should at least be 4 characters long")
	private String password;
	
	private String securityCode;
	
}
