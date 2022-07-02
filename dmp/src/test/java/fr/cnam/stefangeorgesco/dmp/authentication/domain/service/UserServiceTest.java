package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.IUser;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserDAO userDAO;
	
	@MockBean
	private DoctorDAO doctorDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDTO userDTO;
	
	@Autowired
	private Doctor doctor;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Test
	public void testCreateDoctorAccountSuccess() {
		
		userDTO.setId("1");
		userDTO.setPassword("password");
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		
		
		when(userDAO.existsById(userDTO.getId())).thenReturn(true);
		when(doctorDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		when(userDAO.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		assertDoesNotThrow(() -> userService.createDoctorAccount(userDTO));
		
		verify(userDAO, times(1)).existsById(userDTO.getId());
		verify(doctorDAO, times(1)).findById(userDTO.getId());
		verify(userDAO, times(1)).save(any(User.class));
		
		User user = userCaptor.getValue();
		
		assertEquals(userDTO.getId(), user.getId());
		assertTrue(bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword()));
		assertEquals(IUser.ROLE_DOCTOR, user.getRole());
	}

}
