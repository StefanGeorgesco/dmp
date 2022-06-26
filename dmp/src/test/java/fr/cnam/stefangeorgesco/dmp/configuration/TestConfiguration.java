package fr.cnam.stefangeorgesco.dmp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

@Configuration
public class TestConfiguration {
	
	@Bean(name = "userDTO")
	@Scope(value = "prototype")
	UserDTO getUserDTO() {
		return new UserDTO();
	}
	
	@Bean(name = "doctor")
	@Scope(value = "prototype")
	Doctor getDoctor() {
		return new Doctor();
	}
	
	@Bean(name = "user")
	@Scope(value = "prototype")
	User getUser() {
		return new User();
	}
}
