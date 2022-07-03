package fr.cnam.stefangeorgesco.dmp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
public class HelloController {
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@GetMapping("/")
	public Hello hello() {
		return new Hello("Hello World!");
	}
	
	@GetMapping("/encrypt/{code}")
	public String encode(@PathVariable String code) {
		return encoder.encode(code);
	}
	
	@GetMapping("/user")
	public UserDTO getUser() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId("05234");
		userDTO.setUsername("user");
		
		return userDTO;
	}
	
	@GetMapping("/error")
	public void throwException() throws Exception {
		throw new Exception("une erreur est survenue.");
	}
	
	@Data
	@AllArgsConstructor
	private class Hello {
		
		private String message;
		
	}
}
