package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe abstraite parente des objets de transfert de données représentant les dossiers
 * patients et les dossiers de médecins.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public abstract class FileDTO {
	
	@NotBlank(message = "L'identifiant est obligatoire.")
	protected String id;

	/**
	 * Prénom du patient ou du médecin.
	 */
	@NotBlank(message = "Le prénom est obligatoire.")
	protected String firstname;

	/**
	 * Nom du patient ou du médecin.
	 */
	@NotBlank(message = "Le nom est obligatoire.")
	protected String lastname;

	/**
	 * Numéro de téléphone du patient ou du médecin.
	 */
	@NotBlank(message = "Le numéro de téléphone est obligatoire.")
	protected String phone;

	/**
	 * Adresse email du patient ou du médecin.
	 */
	@NotBlank(message = "L'adresse email est obligatoire.")
	@Email(message = "L'adresse email doit être fournie et respecter le format.")
	protected String email;

	/**
	 * Adresse postale du patient ou du médecin.
	 */
	@NotNull(message = "L'adresse est obligatoire.")
	@Valid
	@JsonProperty("address")
	protected AddressDTO addressDTO;

	/**
	 * Code généré lors de la création du dossier (entité), permettant d'authentifier un
	 * utilisateur lors de la création de son compte utilisateur et de valider
	 * son association avec le dossier.
	 */
	protected String securityCode;

}
