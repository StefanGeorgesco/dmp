package fr.cnam.stefangeorgesco.dmp.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Classe de génération des Jwt.
 * 
 * @author Stéfan Georgesco
 *
 */
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

	private String jwtKey;
	
	private String jwtHeader;
	
	private long jwtValidityPeriod;

	public JWTTokenGeneratorFilter(String jwtKey, String jwtHeader, long jwtValidityPeriod) {
		super();
		this.jwtKey = jwtKey;
		this.jwtHeader = jwtHeader;
		this.jwtValidityPeriod = jwtValidityPeriod;
	}

	/**
	 * Génère le Jwt et l'ajoute à l'en-tête
	 * {@code fr.cnam.stefangeorgesco.dmp.constants.SecurityConstants.JWT_HEADER} de
	 * la réponse {@link javax.servlet.http.HttpServletResponse}. Voir
	 * {@code org.springframework.web.filter.OncePerRequestFilter}.
	 */
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
			String jwt = Jwts.builder().setIssuer("fr.cnam.stefangeorgesco.dmp").setSubject("JWT Token")
					.claim("username", authentication.getName())
					.claim("authorities", populateAuthorities(authentication.getAuthorities())).setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + jwtValidityPeriod))
					.signWith(key).compact();
			response.setHeader(jwtHeader, jwt);
		}

		chain.doFilter(request, response);
	}

	/**
	 * Détermine si le filtre doit être inhibé pour la requête. Voir
	 * {@code org.springframework.web.filter.OncePerRequestFilter}.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getMethod().equals("POST") || !request.getServletPath().equals("/login");
	}

	/**
	 * Convertit une collection d'autorisations {@link java.util.Collection<?
	 * extends GrantedAuthority>} en chaîne de caractères énumérant la description
	 * des autorisations, séparées par une virgule.
	 * 
	 * @param collection la collection à convertir.
	 * @return la chaîne de caractères {@link java.lang.String} énumérant la
	 *         description des autorisations.
	 */
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
}
