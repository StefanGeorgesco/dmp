package fr.cnam.stefangeorgesco.dmp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.domain.model.Hello;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public Hello hello() {
		return new Hello("Hello World!");
	}
	
}
