package fr.cnam.stefangeorgesco.dmp.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;

public interface UserDAO extends CrudRepository<User, String> {
	
}
