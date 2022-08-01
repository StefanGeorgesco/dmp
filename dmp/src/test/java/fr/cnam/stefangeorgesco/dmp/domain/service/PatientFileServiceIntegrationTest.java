package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondanceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondanceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/create-correspondances.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-correspondances.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileServiceIntegrationTest {

	@MockBean
	private RNIPPService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private CorrespondanceDAO correspondanceDAO;
	
	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO patientFileDTOResponse;

	@Autowired
	private CorrespondanceDTO correspondanceDTO;

	@Autowired
	private CorrespondanceDTO correspondanceDTOResponse;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;

	@Autowired
	private PatientFile savedPatientFile;

	@Autowired
	private PatientFile persistentPatientFile;

	private long count;
	
	private UUID uuid;
	
	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris Cedex 15");
		addressDTO.setCountry("France-");
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris Cedex 15");
		address.setCountry("France-");
		doctor.setId("D001");
		patientFile.setId("P002");
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("code");
		patientFile.setReferringDoctor(doctor);

		correspondanceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondanceDTO.setDoctorId("D002");
		correspondanceDTO.setPatientFileId("P001");
	}

	@Test
	public void testCreatePatientFileSuccess() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		assertFalse(patientFileDAO.existsById("P002"));

		assertDoesNotThrow(() -> patientFileService.createPatientFile(patientFileDTO));

		assertTrue(patientFileDAO.existsById("P002"));
	}

	@Test
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws CheckException {
		patientFileDAO.save(patientFile);

		doNothing().when(rnippService).checkPatientData(patientFileDTO);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		assertEquals("patient file already exists", ex.getMessage());
	}

	@Test
	public void testUpdatePatientFileSuccess() {
		patientFileDTO.setReferringDoctorId("D002"); // try to change doctor
		patientFileDTO.setId("P001"); // file exists

		assertTrue(patientFileDAO.existsById("P001"));

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		savedPatientFile = patientFileDAO.findById("P001").get();

		// no change in saved object
		assertEquals("P001", savedPatientFile.getId());
		assertEquals("Eric", savedPatientFile.getFirstname());
		assertEquals("Martin", savedPatientFile.getLastname());
		assertEquals("0000", savedPatientFile.getSecurityCode());
		assertEquals("D001", savedPatientFile.getReferringDoctor().getId());

		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		// changes in saved object
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());

		// no change in returned object (except null securityCode)
		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("Eric", patientFileDTOResponse.getFirstname());
		assertEquals("Martin", patientFileDTOResponse.getLastname());
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());

		assertEquals(patientFileDTO.getId(), patientFileDTOResponse.getId());
		// changes in returned object
		assertEquals(patientFileDTO.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFileDTOResponse.getAddressDTO().getCountry());
	}

	@Test
	public void testFindPatientFileSuccess() {
		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.findPatientFile("P001"));

		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("1 rue de la Paix", patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());
		assertNull(patientFileDTOResponse.getSecurityCode());
		assertEquals("1995-05-15", patientFileDTOResponse.getDateOfBirth().toString());
	}

	@Test
	public void testFindPatientFileFailurePatientFileDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFile("P002"));

		assertEquals("patient file not found", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorSuccess() {
		patientFileDTO.setId("P001");

		persistentPatientFile = patientFileDAO.findById("P001").get();
		assertEquals("D001", persistentPatientFile.getReferringDoctor().getId());

		patientFileDTO.setReferringDoctorId("D002");

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updateReferringDoctor(patientFileDTO));

		savedPatientFile = patientFileDAO.findById("P001").get();

		assertEquals("D002", savedPatientFile.getReferringDoctor().getId());
		assertEquals("D002", patientFileDTOResponse.getReferringDoctorId());
	}

	@Test
	public void testUpdateReferringDoctorFailurePatientFileDoesNotExist() {
		patientFileDTO.setId("P002");

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		assertEquals("patient file not found", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorFailureNewDoctorDoesNotExist() {
		patientFileDTO.setId("P001");

		assertTrue(patientFileDAO.existsById("P001"));

		patientFileDTO.setReferringDoctorId("D003");

		FinderException ex = assertThrows(FinderException.class,
				() -> patientFileService.updateReferringDoctor(patientFileDTO));

		assertEquals("doctor not found", ex.getMessage());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound4() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("ma");

		assertEquals(4, patientFiles.size());
		assertEquals("P001", patientFiles.get(0).getId());
		assertEquals("P005", patientFiles.get(1).getId());
		assertEquals("P011", patientFiles.get(2).getId());
		assertEquals("P013", patientFiles.get(3).getId());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound11() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("P0");

		assertEquals(11, patientFiles.size());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound0() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("za");

		assertEquals(0, patientFiles.size());
	}

	@Test
	public void testFindPatientFilesByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {

		List<PatientFileDTO> patientFiles = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("");

		assertEquals(0, patientFiles.size());
	}

	@Test
	public void testCreateCorrespondanceSuccess() {

		count = correspondanceDAO.count();

		assertNull(correspondanceDTO.getId());
		
		doctor = doctorDAO.findById(correspondanceDTO.getDoctorId()).get();

		correspondanceDTOResponse = assertDoesNotThrow(
				() -> patientFileService.createCorrespondance(correspondanceDTO));

		assertEquals(count + 1, correspondanceDAO.count());

		assertEquals(correspondanceDTO.getDateUntil(), correspondanceDTOResponse.getDateUntil());
		assertEquals(correspondanceDTO.getDoctorId(), correspondanceDTOResponse.getDoctorId());
		assertEquals(correspondanceDTO.getPatientFileId(), correspondanceDTOResponse.getPatientFileId());
		assertEquals(doctor.getFirstname(), correspondanceDTOResponse.getDoctorFirstName());
		assertEquals(doctor.getLastname(), correspondanceDTOResponse.getDoctorLastName());
		assertEquals(doctor.getSpecialties().stream().map(Specialty::getDescription).collect(Collectors.toList()), correspondanceDTOResponse.getDoctorSpecialties());

		assertNotNull(correspondanceDTOResponse.getId());
	}

	@Test
	public void testCreateCorrespondanceFailurePatientFileDoesNotExist() {

		correspondanceDTO.setPatientFileId("P002");

		count = correspondanceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> patientFileService.createCorrespondance(correspondanceDTO));

		assertTrue(ex.getMessage().startsWith("correspondance could not be created: "));

		assertEquals(count, correspondanceDAO.count());
	}

	@Test
	public void testCreateCorrespondanceFailureDoctorDoesNotExist() {

		correspondanceDTO.setDoctorId("D003");

		count = correspondanceDAO.count();

		CreateException ex = assertThrows(CreateException.class,
				() -> patientFileService.createCorrespondance(correspondanceDTO));

		assertTrue(ex.getMessage().startsWith("correspondance could not be created: "));

		assertEquals(count, correspondanceDAO.count());
	}
	
	@Test
	public void testDeleteCorrespondanceSuccess() {
		
		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");
		
		count = correspondanceDAO.count();
		
		assertTrue(correspondanceDAO.existsById(uuid));
		
		patientFileService.deleteCorrespondance(uuid);
		
		assertFalse(correspondanceDAO.existsById(uuid));
		
		assertEquals(count - 1, correspondanceDAO.count());
	}
	
	@Test
	public void testFindCorrespondanceSuccess() {
		
		uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");
		
		assertTrue(correspondanceDAO.existsById(uuid));
		
		CorrespondanceDTO correspondanceDTO = assertDoesNotThrow(() -> patientFileService.findCorrespondance(uuid.toString()));
		
		assertEquals("2023-08-14", correspondanceDTO.getDateUntil().toString());
		assertEquals("P004", correspondanceDTO.getPatientFileId());
		assertEquals("Melquisedeque", correspondanceDTO.getDoctorFirstName());
		assertEquals("Nascimento", correspondanceDTO.getDoctorLastName());
		assertEquals(List.of("Specialty 2"), correspondanceDTO.getDoctorSpecialties());
	}
	
	@Test
	public void testFindCorrespondanceFailureCorrespondanceDoesNotExist() {
		
		uuid = UUID.randomUUID();
		
		assertFalse(correspondanceDAO.existsById(uuid));
		
		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findCorrespondance(uuid.toString()));
		
		assertEquals("correspondance not found", ex.getMessage());
	}
	
	@Test
	public void testFindCorrespondancesByPatientFileIdFound3() {
		
		List<CorrespondanceDTO> correspondancesDTO = patientFileService.findCorrespondancesByPatientFileId("P001");
		
		assertEquals(3, correspondancesDTO.size());
		assertEquals("2023-05-02", correspondancesDTO.get(0).getDateUntil().toString());
		assertEquals("e1eb3425-d257-4c5e-8600-b125731c458c", correspondancesDTO.get(1).getId().toString());
		assertEquals("D011", correspondancesDTO.get(2).getDoctorId());
	}
	
	@Test
	public void testFindCorrespondancesByPatientFileIdFound0() {

		List<CorrespondanceDTO> correspondancesDTO = patientFileService.findCorrespondancesByPatientFileId("P055");
		
		assertEquals(0, correspondancesDTO.size());
	}

}
