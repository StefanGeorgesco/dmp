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
	@NotBlank(message = "id is mandatory")
	private String id;
	
	@NotBlank(message = "username is mandatory")
	private String username;
	
	@Column(nullable = false)
	private String role;
	
	@NotBlank(message = "password is mandatory")
	@Size(min=4, message = "password should at least be 4 characters long")
	private String password;
	
	@Column(name = "security_code", nullable = false)
	private String securityCode;
	
}
