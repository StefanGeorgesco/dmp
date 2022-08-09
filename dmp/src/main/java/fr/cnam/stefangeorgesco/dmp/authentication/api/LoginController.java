package fr.cnam.stefangeorgesco.dmp.authentication.api;

import java.security.Principal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	ModelMapper userModelMapper;

	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(Principal principal) throws FinderException {
		
		UserDTO userDTO = userModelMapper.map(userService.findUserByUsername(principal.getName()), UserDTO.class);

		return ResponseEntity.ok(userDTO);

	}

}
