package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_user")
public class User implements IUser {

	@Id
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	@Column(unique=true)
	@NotBlank(message = "Le non utilisateur est obligatoire.")
	private String username;
	
	@Column(nullable = false)
	private String role;
	
	@NotBlank(message = "Le mot de passe est obligatoire.")
	@Size(min=4, message = "Le mot de passe doit contenir au moins 4 caractères.")
	private String password;
	
	@Column(name = "security_code", nullable = false)
	private String securityCode;
	
}
