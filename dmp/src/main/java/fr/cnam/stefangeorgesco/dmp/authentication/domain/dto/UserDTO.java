package fr.cnam.stefangeorgesco.dmp.authentication.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant l'utilisateur.
 * 
 *  @author Stéfan Georgesco    
 *
 */
@Getter
@Setter
public class UserDTO {

	/**
	 * L'identifiant est identique à celui du dossier de médecin ou du dossier patient
	 * auquel l'utilisateur est associé (propriétaire du dossier) et permet cette association.
	 */
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;
	
	/**
	 * Nom d'utilisateur pour l'identification et l'authentification de l'utilisateur.
	 */
	@NotBlank(message = "Le non utilisateur est obligatoire.")
	private String username;
	
	private String role;
	
	@NotBlank(message = "Le mot de passe est obligatoire.")
	@Size(min=4, message = "Le mot de passe doit contenir au moins 4 caractères.")
	private String password;
	
	/**
	 * Ce code généré lors de la création du dossier de médecin ou du dossier patient
	 * auquel l'utilisateur veut s'associer permet d'authentifier l'utilisateur lors de
	 * la création de son compte utilisateur et de valider cette association.
	 */
	private String securityCode;
	
}
