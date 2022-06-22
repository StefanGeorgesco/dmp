package fr.cnam.stefangeorgesco.dmp.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Hello;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public Hello hello() {
		return new Hello("Hello World!");
	}
	
	@GetMapping("/user")
	public UserDTO getUser() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId("05234");
		userDTO.setUsername("user");
		
		return userDTO;
	}
	
	@PostMapping("/user")
	public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
		return userDTO;
	}
	
	@GetMapping("/error")
	public void throwException() throws Exception {
		throw new Exception("une erreur est survenue.");
	}
	
}
