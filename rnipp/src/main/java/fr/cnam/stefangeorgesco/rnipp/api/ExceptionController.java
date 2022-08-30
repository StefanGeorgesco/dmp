package fr.cnam.stefangeorgesco.rnipp.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

/**
 * Classe de gestion des exceptions fournissant des réponses REST aux requêtes
 * en erreur.
 * 
 * @author Stéfan Georgesco
 * 
 */
@ControllerAdvice
@RestController
public class ExceptionController {

	/**
	 * Gestionnaire des erreurs de validation des objets de transfert de données
	 * transmis dans le corps des requêtes REST.
	 * 
	 * @param ex l'exception
	 *           {@link org.springframework.web.bind.MethodArgumentNotValidException}
	 *           levée par une ou plusieurs erreurs de validation d'un objet de
	 *           transfert de données.
	 * @return un objet {@link java.util.Map} donnant un texte d'erreur pour chaque
	 *         attribut de l'objet de transfert de données n'ayant pas respecté les
	 *         contraintes de validation.
	 */
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	/**
	 * Gestionnaire des autres exceptions.
	 * 
	 * @param ex l'exception {@link java.lang.Exception}
	 * @return une réponse {@link }
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public RestResponse handleUnknownException(Exception ex) {
		RestResponse response = new RestResponse(false, ex.getMessage());

		return response;
	}

}
