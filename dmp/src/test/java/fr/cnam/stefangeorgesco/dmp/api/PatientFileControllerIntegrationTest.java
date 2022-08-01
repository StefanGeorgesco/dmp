package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondanceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondanceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.service.RNIPPService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondances.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondances.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
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
	private CorrespondanceDAO correspondanceDAO;

	@Autowired
	private AddressDTO patientAddressDTO;

	@Autowired
	private AddressDTO doctorAddressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private SpecialtyDTO specialtyDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private CorrespondanceDTO correspondanceDTO;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;

	private long count;
	
	private UUID uuid;

	@BeforeEach
	public void setup() {
		patientAddressDTO.setStreet1("1 Rue Lecourbe");
		patientAddressDTO.setZipcode("75015");
		patientAddressDTO.setCity("Paris Cedex 15");
		patientAddressDTO.setCountry("France-");
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(patientAddressDTO);

		doctorAddressDTO.setStreet1("street 1");
		doctorAddressDTO.setZipcode("zipcode");
		doctorAddressDTO.setCity("city");
		doctorAddressDTO.setCountry("country");
		doctorDTO.setAddressDTO(doctorAddressDTO);
		specialtyDTO.setId("id");
		specialtyDTO.setDescription("description");
		doctorDTO.setSpecialtiesDTO(List.of(specialtyDTO));
		doctorDTO.setId("D002");
		doctorDTO.setFirstname("firstname");
		doctorDTO.setLastname("lastname");
		doctorDTO.setEmail("email@email.com");
		doctorDTO.setPhone("0000000000");

		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");
		doctor.setId("D001");
		patientFile.setId("P002");
		patientFile.setFirstname("Firstname");
		patientFile.setLastname("Lastname");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("phone");
		patientFile.setEmail("email@email.com");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("securityCode");
		patientFile.setReferringDoctor(doctor);

		correspondanceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondanceDTO.setDoctorId("D002");
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
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("2000-02-13")));

		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	@WithUserDetails("user")
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws Exception {
		patientFileDAO.save(patientFile);

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		mockMvc.perform(post("/patient-file").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patientFileDTO))).andExpect(status().isConflict())
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
				.andExpect(jsonPath("$.id", is("P001"))).andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.lastname", is("Martin"))).andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				// changes
				.andExpect(jsonPath("$.phone", is(patientFileDTO.getPhone())))
				.andExpect(jsonPath("$.email", is(patientFileDTO.getEmail())))
				.andExpect(jsonPath("$.address.street1", is(patientFileDTO.getAddressDTO().getStreet1())))
				.andExpect(jsonPath("$.address.zipcode", is(patientFileDTO.getAddressDTO().getZipcode())))
				.andExpect(jsonPath("$.address.city", is(patientFileDTO.getAddressDTO().getCity())))
				.andExpect(jsonPath("$.address.country", is(patientFileDTO.getAddressDTO().getCountry())))
				// absent
				.andExpect(jsonPath("$.securityCode").doesNotExist());
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

	@Test
	@WithUserDetails("eric") // P001, ROLE_PATIENT
	public void testGetPatientFileDetailsSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetPatientFileDetailsFailureBadRole() throws Exception {

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetPatientFileDetailsFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/details")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetPatientFileByIdUserIsDoctorSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testGetPatientFileByIdUserIsAdminSuccess() throws Exception {

		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D001")))
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testGetPatientFileByIdFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetPatientFileByIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/P001")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testUpdateReferringDoctorSuccess() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.firstname", is("Eric")))
				.andExpect(jsonPath("$.address.street1", is("1 rue de la Paix")))
				.andExpect(jsonPath("$.referringDoctorId", is("D002"))) // changed
				.andExpect(jsonPath("$.dateOfBirth", is("1995-05-15")))
				.andExpect(jsonPath("$.securityCode").doesNotExist());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testUpdateReferringDoctorFailureUserIsDoctor() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testUpdateReferringDoctorFailureUserIsPatient() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdateReferringDoctorFailureeUnauthenticatedUser() throws Exception {

		mockMvc.perform(put("/patient-file/P001/referring-doctor").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound4UserIsAdmin() throws Exception {
		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", is("P001"))).andExpect(jsonPath("$[1].id", is("P005")))
				.andExpect(jsonPath("$[2].id", is("P011"))).andExpect(jsonPath("$[3].id", is("P013")));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound0() throws Exception {
		mockMvc.perform(get("/patient-file?q=za")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound0SearchStringIsBlank() throws Exception {
		mockMvc.perform(get("/patient-file?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testFindPatientFilesByIdOrFirstnameOrLastnameSuccessFound4UserIsPatientFile() throws Exception {
		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", is("P001"))).andExpect(jsonPath("$[1].id", is("P005")))
				.andExpect(jsonPath("$[2].id", is("P011"))).andExpect(jsonPath("$[3].id", is("P013")));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureMissingQParam() throws Exception {
		mockMvc.perform(get("/patient-file?question=ma")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file?q=ma")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceSuccess() throws Exception {

		count = correspondanceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondance").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.dateUntil", is(correspondanceDTO.getDateUntil().toString())))
				.andExpect(jsonPath("$.doctorId", is("D002")))
				.andExpect(jsonPath("$.doctorFirstName", is("Jean")))
				.andExpect(jsonPath("$.doctorLastName", is("Dupont")))
				.andExpect(jsonPath("$.doctorSpecialties", hasSize(2)))
				.andExpect(jsonPath("$.doctorSpecialties[0]", is("Specialty 1")))
				.andExpect(jsonPath("$.doctorSpecialties[1]", is("Specialty 2")))
				.andExpect(jsonPath("$.patientFileId", is("P001")))
				.andExpect(jsonPath("$.id", hasLength(36)));

		assertEquals(count + 1, correspondanceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailurePatientFileDoesNotExist() throws Exception {

		count = correspondanceDAO.count();

		mockMvc.perform(post("/patient-file/P002/correspondance").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file not found")));

		assertEquals(count, correspondanceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondanceDAO.count();

		mockMvc.perform(post("/patient-file/P004/correspondance").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not referring doctor")));

		assertEquals(count, correspondanceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailureCorrespondingDoctorIsReferringDoctor() throws Exception {

		correspondanceDTO.setDoctorId("D001");
		
		count = correspondanceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondance").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("trying to create correspondance for referring doctor")));

		assertEquals(count, correspondanceDAO.count());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testCreateCorrespondanceFailureBadRolePatient() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondance")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testCreateCorrespondanceFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondance")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testCreateCorrespondanceFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondance")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceSuccess() throws Exception {

		count = correspondanceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("correspondance was deleted")));

		assertEquals(count - 1, correspondanceDAO.count());
		assertFalse(correspondanceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondanceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P004/correspondance/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not referring doctor")));

		assertEquals(count, correspondanceDAO.count());
		assertTrue(correspondanceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailurePatientFileAndCorrespondanceDontMatch() throws Exception {

		count = correspondanceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("correspondance not found for patient file 'P001'")));

		assertEquals(count, correspondanceDAO.count());
		assertTrue(correspondanceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailureCorrespondanceDoesNotExist() throws Exception {

		count = correspondanceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458d"); // does not exist

		assertFalse(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondanceDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("correspondance not found")));

		assertEquals(count, correspondanceDAO.count());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testDeleteCorrespondanceFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString())).andExpect(status().isForbidden());
		
		assertTrue(correspondanceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testDeleteCorrespondanceFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondanceDAO.existsById(uuid));
		
		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString())).andExpect(status().isForbidden());

		assertTrue(correspondanceDAO.existsById(uuid));
}

	@Test
	@WithAnonymousUser
	public void testDeleteCorrespondanceFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondanceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondance/" + uuid.toString())).andExpect(status().isUnauthorized());

		assertTrue(correspondanceDAO.existsById(uuid));
}

}
