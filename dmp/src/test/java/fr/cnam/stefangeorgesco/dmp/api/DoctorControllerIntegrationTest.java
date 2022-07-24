package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private SpecialtyDTO specialtyDTO1;

	@Autowired
	private SpecialtyDTO specialtyDTO2;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	private List<SpecialtyDTO> specialtyDTOs;

	private List<Specialty> specialties;

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO1.setId("S001");
		specialtyDTO1.setDescription("any");
		specialtyDTO2.setId("S002");
		specialtyDTO2.setDescription("any");

		specialtyDTOs = new ArrayList<>();
		specialtyDTOs.add(specialtyDTO1);
		specialtyDTOs.add(specialtyDTO2);

		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");

		doctorDTO.setId("D003");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);

		specialty1.setId("S001");
		specialty2.setId("S002");

		specialties = new ArrayList<>();
		specialties.add(specialty1);
		specialties.add(specialty2);

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");

		doctor.setId("D003");
		doctor.setFirstname("Pierre");
		doctor.setLastname("Martin");
		doctor.setPhone("012345679");
		doctor.setEmail("pierre.martin@docteurs.fr");
		doctor.setSpecialties(specialties);
		doctor.setAddress(address);
		doctor.setSecurityCode("code");
	}

	@AfterEach
	public void tearDown() {
		if (doctorDAO.existsById("D003")) {
			doctorDAO.deleteById("D003");
		}
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorSuccess() throws Exception {

		assertFalse(doctorDAO.existsById("D003"));

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Pierre")))
				.andExpect(jsonPath("$.address.street1", is("1 Rue Lecourbe")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("Specialty 1")))
				.andExpect(jsonPath("$.specialties[1].description", is("Specialty 2")))
				.andExpect(jsonPath("$.securityCode", notNullValue()));

		assertTrue(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorAlreadyExists() throws Exception {
		doctorDAO.save(doctor);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("doctor already exists")));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidFirstname() throws Exception {
		doctorDTO.setFirstname(null);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("firstname is mandatory")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidAddressStreet1() throws Exception {
		doctorDTO.getAddressDTO().setStreet1("");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.address_street1", is("invalid street1")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTOSpecialtiesNull() throws Exception {
		doctorDTO.setSpecialtiesDTO(null);

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties", is("specialties are mandatory")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONoSpecialty() throws Exception {
		doctorDTO.getSpecialtiesDTO().clear();

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties", is("doctor must have at least one specialty")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidSpecialtyId() throws Exception {
		doctorDTO.getSpecialtiesDTO().iterator().next().setId("");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isNotAcceptable())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties0_id", is("id is mandatory")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureSpecialtyDoesNotExist() throws Exception {

		((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).setId("S003");

		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("specialty does not exist")));

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("user")
	public void testCreateDoctorFailureBadRole() throws Exception {
		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithAnonymousUser
	public void testCreateDoctorFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());

		assertFalse(doctorDAO.existsById("D003"));
	}

	@Test
	@WithUserDetails("user") // D001, ROLE_DOCTOR
	public void testUpdateDoctorSuccess() throws Exception {
		assertTrue(doctorDAO.existsById("D001"));

		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// no change (except null securityCode)
				.andExpect(jsonPath("$.id", is("D001"))).andExpect(jsonPath("$.firstname", is("John")))
				.andExpect(jsonPath("$.lastname", is("Smith")))
				.andExpect(jsonPath("$.specialties", hasSize(2))).andExpect(jsonPath("$.specialties[0].id", is("S001")))
				.andExpect(jsonPath("$.specialties[0].description", is("Specialty 1")))
				.andExpect(jsonPath("$.specialties[1].id", is("S002")))
				.andExpect(jsonPath("$.specialties[1].description", is("Specialty 2")))
				// changes
				.andExpect(jsonPath("$.phone", is(doctorDTO.getPhone())))
				.andExpect(jsonPath("$.email", is(doctorDTO.getEmail())))
				.andExpect(jsonPath("$.address.street1", is(doctorDTO.getAddressDTO().getStreet1())))
				.andExpect(jsonPath("$.address.zipcode", is(doctorDTO.getAddressDTO().getZipcode())))
				.andExpect(jsonPath("$.address.city", is(doctorDTO.getAddressDTO().getCity())))
				.andExpect(jsonPath("$.address.country", is(doctorDTO.getAddressDTO().getCountry())))
				// absent
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithUserDetails("eric")
	public void testUpdateDoctorFailureBadRole() throws Exception {
		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdateDoctorFailureUnauthenticatedUser() throws Exception {
		mockMvc.perform(put("/doctor/details").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());

	}

	@Test
	@WithUserDetails("user") // "D001", ROLE_DOCTOR
	public void testGetDoctorDetailsSuccess() throws Exception {

		doctorDTO.setId("D001");

		assertTrue(doctorDAO.existsById("D001"));

		mockMvc.perform(get("/doctor/details")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("John")))
				.andExpect(jsonPath("$.address.street1", is("1 baker street")))
				.andExpect(jsonPath("$.specialties", hasSize(2)))
				.andExpect(jsonPath("$.specialties[0].description", is("Specialty 1")))
				.andExpect(jsonPath("$.specialties[1].description", is("Specialty 2")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

}
