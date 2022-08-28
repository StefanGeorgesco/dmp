package fr.cnam.stefangeorgesco.dmp.constants;

/**
 * Constantes pour la gestion des Jwt.
 * 
 * @author Stéfan Georgesco
 *
 */
public class SecurityConstants {

	/**
	 * La clé de signature des Jwt.
	 */
	public static final String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
	/**
	 * Le nom de l'en-tête comportant le Jwt.
	 */
	public static final String JWT_HEADER = "Authorization";
	/**
	 * La durée de validité des Jwt en ms.
	 */
	public static final Long JWT_VALIDITY_PERIOD = 24L * 60L * 60L * 1000L; // 24hrs
}
