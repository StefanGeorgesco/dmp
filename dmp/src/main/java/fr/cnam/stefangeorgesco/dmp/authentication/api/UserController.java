package fr.cnam.stefangeorgesco.dmp.authentication.api;

import javax.validation.Valid;

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
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/user")
	public ResponseEntity<RestResponse> createAccount(@Valid @RequestBody UserDTO userDTO) throws DuplicateKeyException, FinderException, CheckException {
		
		userService.createAccount(userDTO);
		
		RestResponse response = new RestResponse(HttpStatus.CREATED.value(), "user account was created");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}