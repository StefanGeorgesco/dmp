package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.IUser;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;

@Service
@Validated
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public void createDoctorAccount(UserDTO userDTO) {
		
		userDAO.existsById(userDTO.getId());
		
		doctorDAO.findById(userDTO.getId());
		
		User user = new User();
		user.setId(userDTO.getId());
		user.setPassword(userDTO.getPassword());
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole(IUser.ROLE_DOCTOR);
		
		userDAO.save(user);
	}
	
}
