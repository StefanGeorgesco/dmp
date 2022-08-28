package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Embeddable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant une adresse postale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Embeddable
@Getter
@Setter
public class Address {

	/**
	 * Première ligne d'adresse.
	 */
	@NotBlank(message = "Champ 'street1' invalide.")
	private String street1;

	/**
	 * Seconde ligne d'adresse.
	 */
	@NotNull(message = "Le champ 'street2' ne doit pas être 'null'.")
	private String street2 = "";

	/**
	 * Ville.
	 */
	@NotBlank(message = "Champ 'city' invalide.")
	private String city;

	/**
	 * Etat ou province.
	 */
	@NotNull(message = "Le champ 'state' ne doit pas être 'null'.")
	private String state = "";

	/**
	 * Code postal.
	 */
	@NotBlank(message = "Champ 'zipcode' invalide.")
	private String zipcode;

	/**
	 * Pays.
	 */
	@NotBlank(message = "Champ 'country' invalide.")
	private String country;

}
