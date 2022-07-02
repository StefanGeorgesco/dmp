package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.IUser;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@Service
@Validated
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private DoctorDAO doctorDAO;
	
	@Autowired
	ModelMapper commonModelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public void createDoctorAccount(UserDTO userDTO) throws DuplicateKeyException, FinderException, CheckException  {
		
		if (userDAO.existsById(userDTO.getId())) {
			throw new DuplicateKeyException("user account already exists");
		}
		
		Optional<Doctor> optionalDoctor = doctorDAO.findById(userDTO.getId());
		
		if (optionalDoctor.isEmpty()) {
			throw new FinderException("doctor account does not exist");
		}
		
		Doctor doctor = optionalDoctor.get();
		
		User user = commonModelMapper.map(userDTO, User.class);
		
		doctor.checkUserData(user);
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setSecurityCode(bCryptPasswordEncoder.encode(user.getSecurityCode()));
		user.setRole(IUser.ROLE_DOCTOR);
		
		userDAO.save(user);
	}
	
}
