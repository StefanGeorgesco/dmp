package fr.cnam.stefangeorgesco.dmp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encapsulation des réponses REST lorsqu'aucun objet DTO ne doit être retourné.
 * @author Stéfan Georgesco    
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {
	/**
	 * Statut HTTP de réponse à la requête.
	 */
	public int status;
	/**
	 * Texte de réponse.
	 */
	public String message;
}