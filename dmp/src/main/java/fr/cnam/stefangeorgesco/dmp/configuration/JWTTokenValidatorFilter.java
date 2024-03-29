package fr.cnam.stefangeorgesco.dmp.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Classe de vérification des Jwt.
 * 
 * @author Stéfan Georgesco
 *
 */
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	
	private String jwtKey;
	
	private String jwtHeader;
	
	private UserDetailsService userDetailsService;
	
	public JWTTokenValidatorFilter(String jwtKey, String jwtHeader, UserDetailsService userDetailsService) {
		super();
		this.jwtKey = jwtKey;
		this.jwtHeader = jwtHeader;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Cherche le Jwt dans l'en-tête
	 * {@code fr.cnam.stefangeorgesco.dmp.constants.SecurityConstants.JWT_HEADER} de
	 * la requête {@link javax.servlet.http.HttpServletRequest} et le vérifie. Si la
	 * vérification est positive, met en place l'authentification dans le contexte
	 * de sécurité de l'application. Voir
	 * {@code org.springframework.web.filter.OncePerRequestFilter}.
	 */
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwt = request.getHeader(jwtHeader);
		if (null != jwt) {
			try {
				SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

				String username = String.valueOf(claims.get("username"));
				userDetailsService.loadUserByUsername(username); // check user (still) exists
				String authorities = (String) claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
			}

		}
		chain.doFilter(request, response);
	}

	/**
	 * Détermine si le filtre doit être inhibé pour la requête. Voir
	 * {@code org.springframework.web.filter.OncePerRequestFilter}.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getMethod().equals("POST") && request.getServletPath().equals("/login");
	}

}
