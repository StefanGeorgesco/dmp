package fr.cnam.stefangeorgesco.dmp.authentication.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	@NotBlank(message = "Le non utilisateur est obligatoire.")
	private String username;
	
	private String role;
	
	@NotBlank(message = "Le mot de passe est obligatoire.")
	@Size(min=4, message = "Le mot de passe doit contenir au moins 4 caractères.")
	private String password;
	
	private String securityCode;
	
}
