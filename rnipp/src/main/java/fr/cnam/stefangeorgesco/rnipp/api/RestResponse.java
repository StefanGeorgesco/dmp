package fr.cnam.stefangeorgesco.rnipp.api;

/**
 * Encapsulation des réponses REST.
 * 
 * @author Stéfan Georgesco
 * 
 */
public class RestResponse {

	/**
	 * Résultat de la vérification RNIPP.
	 */
	private boolean result;

	/**
	 * Message de la réponse.
	 */
	private String message;

	public RestResponse() {
		super();
	}

	public RestResponse(boolean result, String message) {
		super();
		this.result = result;
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}