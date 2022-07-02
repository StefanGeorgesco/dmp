package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserDAO userDAO;
	
	@MockBean
	private DoctorDAO doctorDAO;
	
	@MockBean
	private Doctor doctor;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDTO userDTO;
	
	@Autowired
	User user;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@BeforeEach
	public void setup() {
		userDTO.setId("1");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("securityCode");		
	}
	
	@Test
	public void testCreateDoctorAccountSuccess() throws CheckException {
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		
		doNothing().when(doctor).checkUserData(any(User.class));
		when(userDAO.existsById(userDTO.getId())).thenReturn(false);
		when(doctorDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		when(userDAO.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		assertDoesNotThrow(() -> userService.createDoctorAccount(userDTO));
		
		verify(userDAO, times(1)).existsById(userDTO.getId());
		verify(doctorDAO, times(1)).findById(userDTO.getId());
		verify(doctor, times(1)).checkUserData(any(User.class));
		verify(userDAO, times(1)).save(any(User.class));
		
		user = userCaptor.getValue();
		
		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(IUser.ROLE_DOCTOR, user.getRole());
		assertTrue(bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword()));
		assertTrue(bCryptPasswordEncoder.matches(userDTO.getSecurityCode(), user.getSecurityCode()));
	}
	
	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExists() {
		
		when(userDAO.existsById(userDTO.getId())).thenReturn(true);
		
		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> userService.createDoctorAccount(userDTO));
		
		assertEquals("user account already exists", ex.getMessage());
	}
	
	@Test
	public void testCreateDoctorAccountFailureDoctorAccountDoesNotExist() {
		
		when(userDAO.existsById(userDTO.getId())).thenReturn(false);
		when(doctorDAO.findById(userDTO.getId())).thenReturn(Optional.ofNullable(null));
		
		FinderException ex = assertThrows(FinderException.class, () -> userService.createDoctorAccount(userDTO));
		
		assertEquals("doctor account does not exist", ex.getMessage());
	}
	
	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() throws CheckException {
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		
		doThrow(new CheckException("data did not match")).when(doctor).checkUserData(userCaptor.capture());
		when(userDAO.existsById(userDTO.getId())).thenReturn(false);
		when(doctorDAO.findById(userDTO.getId())).thenReturn(Optional.of(doctor));
		
		CheckException ex = assertThrows(CheckException.class, () -> userService.createDoctorAccount(userDTO));
		
		assertEquals("data did not match", ex.getMessage());
		
		verify(userDAO, times(1)).existsById(userDTO.getId());
		verify(doctorDAO, times(1)).findById(userDTO.getId());
		verify(doctor, times(1)).checkUserData(any(User.class));
		
		user = userCaptor.getValue();
		
		assertEquals(userDTO.getId(), user.getId());
		assertEquals(userDTO.getUsername(), user.getUsername());
		assertEquals(userDTO.getSecurityCode(), user.getSecurityCode());
	}

}





