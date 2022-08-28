package fr.cnam.stefangeorgesco.dmp.authentication.api;

import java.security.Principal;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.api.RestResponse;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

/**
 * Contrôleur REST dédié à la gestion des utilisateurs.
 * 
 * @author Stéfan Georgesco
 * 
 */
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ModelMapper userModelMapper;

	/**
	 * Gestionnaire des requêtes POST de création d'un compte utilisateur.
	 * 
	 * @param userDTO l'objet
	 *                {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 *                représentant le compte utilisateur à créer.
	 * @return une réponse {@link RestResponse} encapsulée dans un objet
	 *         org.springframework.http.ResponseEntity avec le statut
	 *         {@link org.springframework.http.HttpStatus#CREATED} en cas de succès.
	 * @throws CreateException
	 * @throws CheckException
	 * @throws FinderException
	 */
	@PostMapping("/user")
	public ResponseEntity<RestResponse> createAccount(@Valid @RequestBody UserDTO userDTO)
			throws FinderException, CheckException, CreateException {

		userService.createAccount(userDTO);

		RestResponse response = new RestResponse(HttpStatus.CREATED.value(), "Le compte utilisateur a été créé.");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Gestionnaire des requêtes POST d'identification / authentification de
	 * l'utilisateur (login)
	 * 
	 * @param principal l'utilisateur authentifié.
	 * @return l'objet
	 *         {@link fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO}
	 *         représentant l'utilisateur authentifié, encapsulé dans un objet
	 *         org.springframework.http.ResponseEntity.
	 * @throws FinderException
	 */
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(Principal principal) throws FinderException {

		UserDTO userDTO = userModelMapper.map(userService.findUserByUsername(principal.getName()), UserDTO.class);

		return ResponseEntity.ok(userDTO);
	}

}
