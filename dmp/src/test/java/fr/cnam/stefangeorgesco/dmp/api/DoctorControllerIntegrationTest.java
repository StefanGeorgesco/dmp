package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class DoctorControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private SpecialtyDAO specilatyDAO;
	
	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private SpecialtyDTO specialtyDTO;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	private List<SpecialtyDTO> specialtyDTOs;

	private List<Specialty> specialties;

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO.setId("S001");
		specialtyDTO.setDescription("A specialty");
		specialtyDTOs = new ArrayList<SpecialtyDTO>();
		specialtyDTOs.add(specialtyDTO);
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
		
		specialty.setId("S001");
		specialty.setDescription("A specialty");
		specilatyDAO.save(specialty);
		specialties = new ArrayList<Specialty>();
		specialties.add(specialty);
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
		if (specilatyDAO.existsById("S001")) {
			specilatyDAO.deleteById("S001");
		}
	}
	
	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorSuccess() throws Exception {
		
		assertFalse(doctorDAO.existsById("D003"));
		
		mockMvc.perform(
				post("/doctor")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctorDTO)))
				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Pierre")))
				.andExpect(jsonPath("$.address.street1", is("1 Rue Lecourbe")))
				.andExpect(jsonPath("$.specialties", hasSize(1)));
		
		assertTrue(doctorDAO.existsById("D003"));
	}
	
	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorAlreadyExists() throws Exception {
		doctorDAO.save(doctor);
		
		mockMvc.perform(
				post("/doctor")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctorDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("doctor already exists")));
	}
	
	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidFirstname() throws Exception {
		doctorDTO.setFirstname(null);
		
		mockMvc.perform(
				post("/doctor")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctorDTO)))
				.andExpect(status().isNotAcceptable()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("firstname is mandatory")));
	}
		
	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidAddressStreet1() throws Exception {
		doctorDTO.getAddressDTO().setStreet1("");
		
		mockMvc.perform(
				post("/doctor")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctorDTO)))
				.andExpect(status().isNotAcceptable()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.address_street1", is("invalid street")));
	}
		
	@Test
	@WithUserDetails("admin")
	public void testCreateDoctorFailureDoctorDTONonValidSpecialtyId() throws Exception {
		doctorDTO.getSpecialtiesDTO().iterator().next().setId("");
		
		mockMvc.perform(
				post("/doctor")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctorDTO)))
				.andExpect(status().isNotAcceptable()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.specialties0_id", is("id is mandatory")));
	}
		
}
