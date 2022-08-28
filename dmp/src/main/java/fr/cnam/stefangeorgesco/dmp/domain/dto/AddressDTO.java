package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * Objet de transfert de données représentant une adresse postale.
 * 
 * @author Stéfan Georgesco
 *
 */
@Getter
@Setter
public class AddressDTO {
	
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
