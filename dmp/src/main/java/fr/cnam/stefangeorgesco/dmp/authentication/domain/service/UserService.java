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
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.File;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@Service
@Validated
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	ModelMapper commonModelMapper;
	
	@Autowired
	ModelMapper userModelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void createAccount(UserDTO userDTO) throws DuplicateKeyException, FinderException, CheckException {

		if (userDAO.existsById(userDTO.getId())) {
			throw new DuplicateKeyException("user account already exists");
		}

		Optional<File> optionalFile = fileDAO.findById(userDTO.getId());

		if (optionalFile.isEmpty()) {
			throw new FinderException("file does not exist");
		}

		File file = optionalFile.get();

		User user = commonModelMapper.map(userDTO, User.class);

		file.checkUserData(user, bCryptPasswordEncoder);

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setSecurityCode(bCryptPasswordEncoder.encode(user.getSecurityCode()));
		if (file instanceof Doctor) {
			user.setRole(IUser.ROLE_DOCTOR);
		} else {
			user.setRole(IUser.ROLE_PATIENT);
		}

		userDAO.save(user);
	}

	public UserDTO findUserByUsername(String username) throws FinderException {
		Optional<User> optionalUser = userDAO.findByUsername(username);
		
		if (optionalUser.isPresent()) {
			return userModelMapper.map(optionalUser.get(), UserDTO.class);
		} else {
			throw new FinderException("user not found");
		}
		
	}

}
