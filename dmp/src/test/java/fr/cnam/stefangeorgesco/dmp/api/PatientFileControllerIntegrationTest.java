package fr.cnam.stefangeorgesco.dmp.api;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasLength;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DiseaseDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.ActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiagnosisDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MailDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PrescriptionDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SymptomDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.service.RNIPPService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-diseases.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-medical-acts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-patient-file-items.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-diseases.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-medical-acts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondences.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
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
	private DoctorDAO doctorDAO;

	@Autowired
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private DiseaseDAO diseaseDAO;

	@Autowired
	private MedicalActDAO medicalActDAO;

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
	private CorrespondenceDTO correspondenceDTO;

	@Autowired
	private MedicalActDTO medicalActDTO;

	@Autowired
	private ActDTO actDTO;

	@Autowired
	private MailDTO mailDTO;

	@Autowired
	private DiseaseDTO diseaseDTO;

	@Autowired
	private DiagnosisDTO diagnosisDTO;
	
	@Autowired
	private PrescriptionDTO prescriptionDTO;
	
	@Autowired
	private SymptomDTO symptomDTO;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private Doctor authoringDoctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private Act act;

	@Autowired
	private Mail mail;

	private long count;

	private UUID uuid;

	private String comment;

	private String text;

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

		correspondenceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondenceDTO.setDoctorId("D002");

		comment = "A comment";
		text = "A text";
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

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.dateUntil", is(correspondenceDTO.getDateUntil().toString())))
				.andExpect(jsonPath("$.doctorId", is("D002"))).andExpect(jsonPath("$.doctorFirstName", is("Jean")))
				.andExpect(jsonPath("$.doctorLastName", is("Dupont")))
				.andExpect(jsonPath("$.doctorSpecialties", hasSize(2)))
				.andExpect(jsonPath("$.doctorSpecialties[0]", is("chirurgie vasculaire")))
				.andExpect(jsonPath("$.doctorSpecialties[1]", is("neurochirurgie")))
				.andExpect(jsonPath("$.patientFileId", is("P001"))).andExpect(jsonPath("$.id", hasLength(36)));

		assertEquals(count + 1, correspondenceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailurePatientFileDoesNotExist() throws Exception {

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P002/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file not found")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P004/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not referring doctor")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateCorrespondanceFailureCorrespondingDoctorIsReferringDoctor() throws Exception {

		correspondenceDTO.setDoctorId("D001");

		count = correspondenceDAO.count();

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("trying to create correspondence for referring doctor")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testCreateCorrespondanceFailureBadRolePatient() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testCreateCorrespondanceFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testCreateCorrespondanceFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(post("/patient-file/P001/correspondence").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(correspondenceDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceSuccess() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.status", is(200)))
				.andExpect(jsonPath("$.message", is("correspondence was deleted")));

		assertEquals(count - 1, correspondenceDAO.count());
		assertFalse(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailureDoctorIsNotReferringDoctor() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P004/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not referring doctor")));

		assertEquals(count, correspondenceDAO.count());
		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailurePatientFileAndCorrespondanceDontMatch() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("a376a45f-17d3-4b75-ad08-6b1da02616b6"); // P004 (referring D004), corresponding D002

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("correspondence not found for patient file 'P001'")));

		assertEquals(count, correspondenceDAO.count());
		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testDeleteCorrespondanceFailureCorrespondanceDoesNotExist() throws Exception {

		count = correspondenceDAO.count();

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458d"); // does not exist

		assertFalse(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(correspondenceDTO)))
				.andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("correspondence not found")));

		assertEquals(count, correspondenceDAO.count());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testDeleteCorrespondanceFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isForbidden());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testDeleteCorrespondanceFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isForbidden());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithAnonymousUser
	public void testDeleteCorrespondanceFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("e1eb3425-d257-4c5e-8600-b125731c458c"); // P001 (referring D001), corresponding D007

		assertTrue(correspondenceDAO.existsById(uuid));

		mockMvc.perform(delete("/patient-file/P001/correspondence/" + uuid.toString()))
				.andExpect(status().isUnauthorized());

		assertTrue(correspondenceDAO.existsById(uuid));
	}

	@Test
	@WithUserDetails("user") // "D001", ROLE_DOCTOR
	public void testFindCorrespondancesByPatientFileIdSuccessUserIsReferringDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is("5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b")))
				.andExpect(jsonPath("$[0].doctorId", is("D002")))
				.andExpect(jsonPath("$[0].doctorFirstName", is("Jean")))
				.andExpect(jsonPath("$[0].doctorLastName", is("Dupont")))
				.andExpect(jsonPath("$[0].dateUntil", is("2023-05-02")));
	}

	@Test
	@WithUserDetails("user") // "D001", ROLE_DOCTOR
	public void testFindCorrespondancesByPatientFileIdSuccessUserIsActiveCorrespondingDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/P006/correspondence"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].id", is("8ea37abc-052d-4bc3-9aa1-f9a47e366e11")))
				.andExpect(jsonPath("$[1].doctorId", is("D001")))
				.andExpect(jsonPath("$[1].doctorFirstName", is("John")))
				.andExpect(jsonPath("$[1].doctorLastName", is("Smith")))
				.andExpect(jsonPath("$[1].dateUntil", is("2027-05-07")));
	}

	@Test
	@WithUserDetails("user") // "D001", ROLE_DOCTOR
	public void testFindCorrespondancesByPatientFileIdFailureCorrespondenceExpired() throws Exception {

		mockMvc.perform(get("/patient-file/P013/correspondence"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("user is not referring nor corresponding doctor")));
	}

	@Test
	@WithUserDetails("user") // "D001", ROLE_DOCTOR
	public void testFindCorrespondancesByPatientFileIdReturns0UserIsNotReferringNorCorrespondingDoctor()
			throws Exception {

		mockMvc.perform(get("/patient-file/P004/correspondence"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("user is not referring nor corresponding doctor")));
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testFindCorrespondancesByPatientFileIdFailureBadRolePatient() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindCorrespondancesByPatientFileIdFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindCorrespondancesByPatientFileIdFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/P001/correspondence")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT, P001
	public void testFindPatientCorrespondancesSuccess() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is("5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b")))
				.andExpect(jsonPath("$[0].doctorId", is("D002")))
				.andExpect(jsonPath("$[0].doctorFirstName", is("Jean")))
				.andExpect(jsonPath("$[0].doctorLastName", is("Dupont")))
				.andExpect(jsonPath("$[0].dateUntil", is("2023-05-02")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testFindPatientCorrespondancesFailureBadRoleDoctor() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testFindPatientCorrespondancesFailureBadRoleAdmin() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testFindPatientCorrespondancesFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/patient-file/details/correspondence")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseaseByIdSuccessUserIsDoctor() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is("J01")))
				.andExpect(jsonPath("$.description", is("Sinusite aiguë")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseaseByIdFailureUserIsDoctorDiseaseDoesNotExist() throws Exception {

		assertFalse(diseaseDAO.existsById("J000"));

		mockMvc.perform(get("/disease/J000")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("disease not found")));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testGetDiseaseByIdFailureUserIsAdmin() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testGetDiseaseByIdFailureUserIsPatient() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetDiseaseByIdFailureUnauthenticatedUser() throws Exception {

		assertTrue(diseaseDAO.existsById("J01"));

		mockMvc.perform(get("/disease/J01")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound8UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(8)))
				.andExpect(jsonPath("$[2].id", is("J011")))
				.andExpect(jsonPath("$[2].description", is("Sinusite frontale aiguë")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionLimit5UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite&limit=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound0UserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=mas")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionFound0SearchStringIsBlankUserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetDiseasesByIdOrDescriptionErrorQSearchStringIsAbsentUserIsDoctor() throws Exception {

		mockMvc.perform(get("/disease")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testGetDiseasesByIdOrDescriptionFailureUserIsAdmin() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testGetDiseasesByIdOrDescriptionFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetDiseasesByIdOrDescriptionFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/disease?q=sinusite")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActByIdSuccessUserIsDoctor() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is("HCAE201")))
				.andExpect(jsonPath("$.description", is(
						"Dilatation de sténose du conduit d'une glande salivaire par endoscopie [sialendoscopie] ")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActByIdFailureUserIsDoctorMedicalActDoesNotExist() throws Exception {

		assertFalse(medicalActDAO.existsById("H000000"));

		mockMvc.perform(get("/medical-act/H000000")).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("medical act not found")));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testGetMedicalActByIdFailureUserIsAdmin() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testGetMedicalActByIdFailureUserIsPatient() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetMedicalActByIdFailureUnauthenticatedUser() throws Exception {

		assertTrue(medicalActDAO.existsById("HCAE201"));

		mockMvc.perform(get("/medical-act/HCAE201")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound9UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(9)))
				.andExpect(jsonPath("$[2].id", is("HBQK389"))).andExpect(jsonPath("$[2].description", is(
						"Radiographie intrabuccale rétroalvéolaire et/ou rétrocoronaire d'un secteur de 1 à 3 dents contigües")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionLimit5UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio&limit=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound0UserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=rid")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionFound0SearchStringIsBlankUserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act?q=")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR
	public void testGetMedicalActsByIdOrDescriptionErrorQSearchStringIsAbsentUserIsDoctor() throws Exception {

		mockMvc.perform(get("/medical-act")).andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testGetMedicalActsByIdOrDescriptionFailureUserIsAdmin() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testGetMedicalActsByIdOrDescriptionFailureUserIsPatient() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testGetMedicalActsByIdOrDescriptionFailureUnauthenticatedUser() throws Exception {

		mockMvc.perform(get("/medical-act?q=radio")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateActSuccessUserIsReferringDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setMedicalActDTO(medicalActDTO);

		assertNull(actDTO.getId());
		assertNull(actDTO.getAuthoringDoctorId());
		assertNull(actDTO.getPatientFileId());

		authoringDoctor = doctorDAO.findById("D001").get();

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is(now.toString())))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P001")));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateActSuccessUserIsActiveCorrespondingDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		assertNull(actDTO.getId());
		assertNull(actDTO.getPatientFileId());

		authoringDoctor = doctorDAO.findById(actDTO.getAuthoringDoctorId()).get();

		mockMvc.perform(post("/patient-file/P006/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is(now.toString())))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P006")));

		assertEquals(count + 1, patientFileItemDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateActFailureUserCorrespondanceExpired() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P013/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("user is not referring nor corresponding doctor")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateActFailureUserIsNotReferringNorCorrespondingDoctor() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P004/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("user is not referring nor corresponding doctor")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testCreateActFailurePatientFileDoesNotExist() throws Exception {

		count = patientFileItemDAO.count();

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P002/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("patient file not found")));

		assertEquals(count, patientFileItemDAO.count());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testCreatePatientFileItemFailureBadRolePatient() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testCreatePatientFileItemFailureBadRoleAdmin() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testCreatePatientFileItemFailureUnauthenticatedUser() throws Exception {

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");
		actDTO.setDate(now);
		actDTO.setComments("comments on this act");
		actDTO.setAuthoringDoctorId("D001");
		actDTO.setMedicalActDTO(medicalActDTO);

		mockMvc.perform(post("/patient-file/P001/item").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemSuccessUserIsReferringAndAuthor() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID()); // not taken into account
		actDTO.setDate(now); // can not be updated
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002"); // can not be updated
		actDTO.setPatientFileId("P002"); // can not be updated

		authoringDoctor = doctorDAO.findById("D001").get();

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("act")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is("2021-10-11")))
				.andExpect(jsonPath("$.comments", is(actDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.medicalAct.id", is(actDTO.getMedicalActDTO().getId())))
				.andExpect(jsonPath("$.medicalAct.description",
						is("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire")))
				.andExpect(jsonPath("$.patientFileId", is("P005")));

		act = (Act) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, act.getComments());
		assertEquals("2021-10-11", act.getDate().toString());
		assertEquals(authoringDoctor.getId(), act.getAuthoringDoctor().getId());
		assertEquals("HBSD001", act.getMedicalAct().getId());
		assertEquals("Hémostase gingivoalvéolaire secondaire à une avulsion dentaire",
				act.getMedicalAct().getDescription());
		assertEquals("P005", act.getPatientFile().getId());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemSuccessUserIsActiveCorrespondentAndAuthor() throws Exception {

		uuid = UUID.fromString("7f331dd1-0950-4991-964c-2383ba92699e");

		LocalDate now = LocalDate.now();

		mailDTO.setId(UUID.randomUUID()); // not taken into account
		mailDTO.setDate(now); // can not be updated
		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId("D004");
		mailDTO.setAuthoringDoctorId("D002"); // can not be updated
		mailDTO.setPatientFileId("P002"); // can not be updated

		authoringDoctor = doctorDAO.findById("D001").get();

		mockMvc.perform(put("/patient-file/P006/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mailDTO))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.@type", is("mail")))
				.andExpect(jsonPath("$.id", hasLength(36))).andExpect(jsonPath("$.date", is("2022-05-27")))
				.andExpect(jsonPath("$.comments", is(mailDTO.getComments())))
				.andExpect(jsonPath("$.authoringDoctorId", is(authoringDoctor.getId())))
				.andExpect(jsonPath("$.authoringDoctorFirstname", is(authoringDoctor.getFirstname())))
				.andExpect(jsonPath("$.authoringDoctorLastname", is(authoringDoctor.getLastname())))
				.andExpect(jsonPath("$.text", is(text))).andExpect(jsonPath("$.recipientDoctorId", is("D004")))
				.andExpect(jsonPath("$.recipientDoctorFirstname", is("Leah")))
				.andExpect(jsonPath("$.recipientDoctorLastname", is("Little")))
				.andExpect(jsonPath("$.recipientDoctorSpecialties", hasSize(1)))
				.andExpect(jsonPath("$.recipientDoctorSpecialties[0]", is("gériatrie")))
				.andExpect(jsonPath("$.patientFileId", is("P006")));

		mail = (Mail) patientFileItemDAO.findById(uuid).get();

		assertEquals(comment, mail.getComments());
		assertEquals("2022-05-27", mail.getDate().toString());
		assertEquals(text, mail.getText());
		assertEquals("D004", mail.getRecipientDoctor().getId());
		assertEquals("Leah", mail.getRecipientDoctor().getFirstname());
		assertEquals("Little", mail.getRecipientDoctor().getLastname());
		assertEquals("gériatrie", mail.getRecipientDoctor().getSpecialties().iterator().next().getDescription());
		assertEquals(authoringDoctor.getId(), mail.getAuthoringDoctor().getId());
		assertEquals("P006", mail.getPatientFile().getId());
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailureWrongPayloadType() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // type Act

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		mailDTO.setId(UUID.randomUUID());
		mailDTO.setDate(now);
		mailDTO.setComments(comment);
		mailDTO.setText(text);
		mailDTO.setRecipientDoctorId("D004");
		mailDTO.setAuthoringDoctorId("D002");
		mailDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mailDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file items types do not match")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemDoesNotExist() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22"); // does not exist

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID()); // not taken into account
		actDTO.setDate(now); // can not be updated
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002"); // can not be updated
		actDTO.setPatientFileId("P002"); // can not be updated

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file item not found")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemFailureUserIsReferringButNotAuthor() throws Exception {

		uuid = UUID.fromString("c793da7f-5ca8-41f5-a0f0-1cc77b34b6fe");

		LocalDate now = LocalDate.now();

		diseaseDTO.setId("J010");

		diagnosisDTO.setId(UUID.randomUUID());
		diagnosisDTO.setDate(now);
		diagnosisDTO.setComments(comment);
		diagnosisDTO.setDiseaseDTO(diseaseDTO);
		diagnosisDTO.setAuthoringDoctorId("D002");
		diagnosisDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(diagnosisDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message",
						is("user is not the author of patient file item and can not modify it")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemFailureUserIsActiveCorrespondentButNotAuthor() throws Exception {

		uuid = UUID.fromString("643bce5b-9c74-4e6f-8ae2-5809a31bedb4");

		LocalDate now = LocalDate.now();

		prescriptionDTO.setId(UUID.randomUUID());
		prescriptionDTO.setDate(now);
		prescriptionDTO.setComments(comment);
		prescriptionDTO.setDescription("prescription description");
		prescriptionDTO.setAuthoringDoctorId("D002");
		prescriptionDTO.setPatientFileId("P002");	

		mockMvc.perform(put("/patient-file/P006/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(prescriptionDTO))).andExpect(status().isConflict())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not the author of patient file item and can not modify it")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemFailureUserIsCorrespondentAndAuthorButNotActiveCorrespondent() throws Exception {

		uuid = UUID.fromString("142763cf-6eeb-47a5-b8f8-8ec85f0025c4");

		LocalDate now = LocalDate.now();

		symptomDTO.setId(UUID.randomUUID());
		symptomDTO.setDate(now);
		symptomDTO.setComments(comment);
		symptomDTO.setDescription("symptom description");
		symptomDTO.setAuthoringDoctorId("D002");
		symptomDTO.setPatientFileId("P002");	

		mockMvc.perform(put("/patient-file/P013/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(symptomDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("user is not referring nor corresponding doctor")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemFailureUserIsNotReferringNorCorrespondingDoctor() throws Exception {

		uuid = UUID.fromString("b7bdb6e4-da4b-47f9-9b67-cfd67567aaca");

		LocalDate now = LocalDate.now();

		diseaseDTO.setId("J010");

		diagnosisDTO.setId(UUID.randomUUID());
		diagnosisDTO.setDate(now);
		diagnosisDTO.setComments(comment);
		diagnosisDTO.setDiseaseDTO(diseaseDTO);
		diagnosisDTO.setAuthoringDoctorId("D002");
		diagnosisDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P012/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(diagnosisDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message",
						is("user is not referring nor corresponding doctor")));
	}

	@Test
	@WithUserDetails("user") // ROLE_DOCTOR, D001
	public void testUpdatePatientFileItemUserIsReferringAndAuthorFailurePatientFileItemAndPatientFileDontMatch() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c"); // P005

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P001/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message", is("patient file item not found for patient file 'P001'")));
	}

	@Test
	@WithUserDetails("admin") // ROLE_ADMIN
	public void testUpdatePatientFileItemFailureBadRoleAdmin() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithUserDetails("eric") // ROLE_PATIENT
	public void testUpdatePatientFileItemFailureBadRolePatient() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void testUpdatePatientFileItemFailureUnauthenticatedUser() throws Exception {

		uuid = UUID.fromString("1b57e70f-8eb0-4a97-99c6-5d44f138c22c");

		LocalDate now = LocalDate.now();

		medicalActDTO.setId("HBSD001");

		actDTO.setId(UUID.randomUUID());
		actDTO.setDate(now);
		actDTO.setComments(comment);
		actDTO.setMedicalActDTO(medicalActDTO);
		actDTO.setAuthoringDoctorId("D002");
		actDTO.setPatientFileId("P002");

		mockMvc.perform(put("/patient-file/P005/item/" + uuid.toString()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actDTO))).andExpect(status().isUnauthorized());
	}

}
