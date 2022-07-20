package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.service.RNIPPService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private RNIPPService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private AddressDTO addressDTO;
	
	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris Cedex 15");
		addressDTO.setCountry("France-");
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);

		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");
		doctor.setId("D001");
		patientFile.setId("P002");
		patientFile.setFirstname("Firstname");
		patientFile.setLastname("Lastname");
		patientFile.setPhone("phone");
		patientFile.setEmail("email@email.com");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("securityCode");
		patientFile.setReferringDoctor(doctor);
	}

	@AfterEach
	public void teardown() {
		if (patientFileDAO.existsById("P002")) {
			patientFileDAO.deleteById("P002");
		}
	}

	@Test
	@WithUserDetails("user") // D001, ROLE_DOCTOR
	public void testCreatePatientFileSuccess() throws Exception {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		assertFalse(patientFileDAO.existsById("P002"));

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Patrick")))
				.andExpect(jsonPath("$.address.street1", is("1 Rue Lecourbe")))
				.andExpect(jsonPath("$.securityCode", notNullValue()))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")));

		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithUserDetails("user")
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws Exception {
		patientFileDAO.save(patientFile);

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file already exists")));
	}

	@Test
	@WithUserDetails("user")
	public void testCreatePatientFileFailurePatientFileDTONonValidLastname() throws Exception {
		patientFileDTO.setLastname("");

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.lastname", is("lastname is mandatory")));

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithUserDetails("user")
	public void testCreatePatientFileFailurePatientFileDTONonValidAddressCountry() throws Exception {
		patientFileDTO.getAddressDTO().setCountry(null);

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.address_country", is("invalid country")));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreatePatientFileFailureBadRole() throws Exception {
		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isForbidden());

		assertFalse(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithAnonymousUser
	public void testCreatePatientFileFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isUnauthorized());

	}
	
	@Test
	@WithUserDetails("eric") // P001, ROLE_PATIENT
	public void testUpdatePatientFileSuccess() throws Exception {
		patientFileDTO.setReferringDoctorId("D002"); // try to change doctor
		
		assertTrue(patientFileDAO.existsById("P001"));
		
		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// no change (except null securityCode)
				.andExpect(jsonPath("$.id", is("P001")))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.lastname", is("Martin")))
				.andExpect(jsonPath("$.securityCode", nullValue()))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				// changes
				.andExpect(jsonPath("$.phone", is(patientFileDTO.getPhone())))
				.andExpect(jsonPath("$.email", is(patientFileDTO.getEmail())))
				.andExpect(jsonPath("$.address.street1", is(patientFileDTO.getAddressDTO().getStreet1())))
				.andExpect(jsonPath("$.address.zipcode", is(patientFileDTO.getAddressDTO().getZipcode())))
				.andExpect(jsonPath("$.address.city", is(patientFileDTO.getAddressDTO().getCity())))
				.andExpect(jsonPath("$.address.country", is(patientFileDTO.getAddressDTO().getCountry())));
	}
	
	@Test
	@WithUserDetails("user")
	public void testUpdatePatientFileFailureBadRole() throws Exception {
		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isForbidden());
	}
		
	@Test
	@WithAnonymousUser
	public void testUpdatePatientFileFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(put("/patient-file/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isUnauthorized());

	}
	
}
