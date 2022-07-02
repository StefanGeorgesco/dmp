package fr.cnam.stefangeorgesco.dmp.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceIntegrationTest {

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	private Address address;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Doctor doctor;

	@Autowired
	User user;

	private List<Specialty> specialties;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeEach
	public void setup() {
		specialty.setId("s001");
		specialty.setDescription("A specialty");

		specialtyDAO.save(specialty);

		specialties = new ArrayList<>();
		specialties.add(specialty);

		address.setStreet1("street");
		address.setCity("city");
		address.setZipcode("zip");
		address.setCountry("country");

		doctor.setId("id");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(address);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));

		doctorDAO.save(doctor);

		userDTO.setId("id");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("12345678");
	}

	@AfterEach
	public void tearDown() {
		doctorDAO.delete(doctor);
		specialtyDAO.delete(specialty);
		if (userDAO.existsById("id")) {
			userDAO.deleteById("id");
		}
	}

	@Test
	public void testCreateDoctorAccountSuccess() {

		assertFalse(userDAO.existsById("id"));

		assertDoesNotThrow(() -> userService.createDoctorAccount(userDTO));

		assertTrue(userDAO.existsById("id"));
	}

	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExists() {

		user.setId("id");
		user.setUsername("John");
		user.setRole("USER");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);

		assertThrows(DuplicateKeyException.class, () -> userService.createDoctorAccount(userDTO));
	}

	@Test
	public void testCreateDoctorAccountFailureDoctorAccountDoesNotExist() {

		doctorDAO.delete(doctor);

		assertThrows(FinderException.class, () -> userService.createDoctorAccount(userDTO));

		assertFalse(userDAO.existsById("id"));
	}
	
	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() {
		
		userDTO.setSecurityCode("1111");
		
		assertThrows(CheckException.class, () -> userService.createDoctorAccount(userDTO));

		assertFalse(userDAO.existsById("id"));
	}

}
