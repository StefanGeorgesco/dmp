package fr.cnam.stefangeorgesco.dmp.authentication.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dao.UserDAO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	UserDTO userDTO;
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private FileDAO fileDAO;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	private Address doctorAddress;
	
	@Autowired
	private Address patientFileAddress;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Doctor doctor;
	
	@Autowired
	private PatientFile patientFile;

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

		doctorAddress.setStreet1("street");
		doctorAddress.setCity("city");
		doctorAddress.setZipcode("zip");
		doctorAddress.setCountry("country");

		doctor.setId("doctorId");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode(bCryptPasswordEncoder.encode("12345678"));

		fileDAO.save(doctor);
		
		patientFileAddress.setStreet1("1 rue de la Paix");
		patientFileAddress.setCity("Paris");
		patientFileAddress.setZipcode("75001");
		patientFileAddress.setCountry("France");
		
		patientFile.setId("patientFileId");
		patientFile.setFirstname("Eric");
		patientFile.setLastname("Martin");
		patientFile.setPhone("1111111111");
		patientFile.setEmail("eric.martin@free.fr");
		patientFile.setAddress(patientFileAddress);
		patientFile.setReferringDoctor(doctor);
		patientFile.setSecurityCode(bCryptPasswordEncoder.encode("7890"));

		fileDAO.save(patientFile);
		
		userDTO.setId("doctorId");
		userDTO.setUsername("username");
		userDTO.setPassword("password");
		userDTO.setSecurityCode("12345678");
	}

	@AfterEach
	public void tearDown() {
		fileDAO.delete(patientFile);
		fileDAO.delete(doctor);
		specialtyDAO.delete(specialty);
		if (userDAO.existsById("doctorId")) {
			userDAO.deleteById("doctorId");
		}
		if (userDAO.existsById("patientFileId")) {
			userDAO.deleteById("patientFileId");
		}
	}

	@Test
	public void testCreateDoctorAccountSuccess() throws Exception {
		
		assertFalse(userDAO.existsById("doctorId"));
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user account was created")));
		
		assertTrue(userDAO.existsById("doctorId"));
	}
	

	@Test
	public void testCreatePatientAccountSuccess() throws Exception {
		
		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");
		
		assertFalse(userDAO.existsById("patientFileId"));
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user account was created")));
		
		assertTrue(userDAO.existsById("patientFileId"));
	}

	@Test
	public void testCreateDoctorAccountFailureUserDTONonValid() throws Exception {
		
		userDTO.setId(null);
		userDTO.setUsername("");
		userDTO.setPassword("123");
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isNotAcceptable()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is("id is mandatory")))
				.andExpect(jsonPath("$.username", is("username is mandatory")))
				.andExpect(jsonPath("$.password", is("password should at least be 4 characters long")));
		
		assertFalse(userDAO.existsById("doctorId"));
	}
	
	@Test
	public void testCreateDoctorAccountFailureUserAccountAlreadyExists() throws Exception {
		
		user.setId("doctorId");
		user.setUsername("John");
		user.setRole("USER");
		user.setPassword("0123");
		user.setSecurityCode("0000");
		userDAO.save(user);
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user account already exists")));
		
	}
	
	@Test
	public void testCreatePatientAccountFailureFileDoesNotExist() throws Exception {
		
		fileDAO.delete(patientFile);
		
		userDTO.setId("patientFileId");
		userDTO.setSecurityCode("7890");
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("file does not exist")));
		
		assertFalse(userDAO.existsById("patientFileId"));
	}
	
	@Test
	public void testCreateDoctorAccountFailureCheckUserDataError() throws Exception {
		
		userDTO.setSecurityCode("1111");
		
		mockMvc.perform(
				post("/user")
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("data did not match")));
		
		assertFalse(userDAO.existsById("doctorId"));
	}
	
}
