package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondanceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondanceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PatientFileServiceTest {

	@MockBean
	private RNIPPService rnippService;

	@MockBean
	private PatientFileDAO patientFileDAO;

	@MockBean
	private DoctorDAO doctorDAO;
	
	@MockBean
	private CorrespondanceDAO correspondanceDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO patientFileDTO1;

	@Autowired
	private PatientFileDTO patientFileDTO2;

	@Autowired
	private PatientFileDTO patientFileDTOResponse;
	
	@Autowired
	private CorrespondanceDTO correspondanceDTO;

	@Autowired
	private CorrespondanceDTO correspondanceDTOResponse;
	
	@Autowired
	private Doctor doctor;

	@Autowired
	private Doctor newDoctor;
	
	@Autowired
	private Address address;

	@Autowired
	private PatientFile persistentPatientFile;

	@Autowired
	private PatientFile savedPatientFile;
	
	@Autowired
	private PatientFile patientFile1;

	@Autowired
	private PatientFile patientFile2;
	
	@Autowired
	private Correspondance savedCorrespondance;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<PatientFile> patientFileCaptor = ArgumentCaptor.forClass(PatientFile.class);

	private ArgumentCaptor<Correspondance> correspondanceCaptor = ArgumentCaptor.forClass(Correspondance.class);

	private UUID uuid;
	
	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");

		address.setStreet1("street 1");
		address.setZipcode("zipcode");
		address.setCity("City");
		address.setCountry("Country");
		doctor.setId("D001");
		persistentPatientFile.setId(patientFileDTO.getId());
		persistentPatientFile.setFirstname("firstname");
		persistentPatientFile.setLastname("lastname");
		persistentPatientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
		persistentPatientFile.setAddress(address);
		persistentPatientFile.setSecurityCode("securityCode");
		persistentPatientFile.setReferringDoctor(doctor);
		
		patientFile1.setId("ID_1");
		patientFile1.setFirstname("firstname_1");
		patientFile1.setLastname("lastname_1");
		patientFile1.setDateOfBirth(LocalDate.of(2000, 2, 13));
		patientFile1.setAddress(address);
		patientFile1.setSecurityCode("securityCode_1");
		patientFile1.setReferringDoctor(doctor);

		patientFile2.setId("ID_2");
		patientFile2.setFirstname("firstname_2");
		patientFile2.setLastname("lastname_2");
		patientFile2.setDateOfBirth(LocalDate.of(1995, 8, 21));
		patientFile2.setAddress(address);
		patientFile2.setSecurityCode("securityCode_2");
		patientFile2.setReferringDoctor(doctor);
		
		correspondanceDTO.setDateUntil(LocalDate.now().plusDays(1));
		correspondanceDTO.setDoctorId("D002");
		correspondanceDTO.setPatientFileId("P002");
}

	@Test
	public void testCreatePatientFileSuccess() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);
		when(patientFileDAO.existsById(patientFileDTO.getId())).thenReturn(false);
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.createPatientFile(patientFileDTO));

		verify(rnippService, times(1)).checkPatientData(patientFileDTO);
		verify(patientFileDAO, times(1)).existsById(patientFileDTO.getId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		assertEquals(patientFileDTO.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(patientFileDTO.getLastname(), savedPatientFile.getLastname());
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());
		assertEquals(patientFileDTO.getReferringDoctorId(), savedPatientFile.getReferringDoctor().getId());

		assertNotNull(patientFileDTOResponse.getSecurityCode());
		assertTrue(patientFileDTOResponse.getSecurityCode().length() >= 10);
		assertTrue(bCryptPasswordEncoder.matches(patientFileDTOResponse.getSecurityCode(), savedPatientFile.getSecurityCode()));
	}

	@Test
	public void testCreatePatientFileFailureRNIPPServiceThrowsException() throws CheckException {
		doThrow(new CheckException("patient data did not match")).when(rnippService).checkPatientData(patientFileDTO);

		CheckException ex = assertThrows(CheckException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		assertEquals("patient data did not match", ex.getMessage());
	}

	@Test
	public void testCreatePatientFileFailurePatientFileAlreadyExist() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);
		when(patientFileDAO.existsById(patientFileDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> patientFileService.createPatientFile(patientFileDTO));

		verify(rnippService, times(1)).checkPatientData(patientFileDTO);
		verify(patientFileDAO, times(1)).existsById(patientFileDTO.getId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));

		assertEquals("patient file already exists", ex.getMessage());
	}

	@Test
	public void testUpdatePatientFileSuccess() {
		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.of(persistentPatientFile));
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), savedPatientFile.getLastname());
		assertEquals(persistentPatientFile.getSecurityCode(), savedPatientFile.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(), savedPatientFile.getReferringDoctor().getId());

		// updated - compared to captured saved object
		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getFirstname(), patientFileDTOResponse.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), patientFileDTOResponse.getLastname());
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(), patientFileDTOResponse.getReferringDoctorId());

		// updated - compared to response DTO object
		assertEquals(patientFileDTO.getId(), patientFileDTOResponse.getId());
		assertEquals(patientFileDTO.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFileDTOResponse.getAddressDTO().getCountry());
	}

	@Test
	public void testFindPatientFileSuccess() {
		when(patientFileDAO.findById("P001")).thenReturn(Optional.of(persistentPatientFile));

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.findPatientFile("P001"));

		verify(patientFileDAO, times(1)).findById("P001");

		assertEquals("P001", patientFileDTOResponse.getId());
		assertEquals("firstname", patientFileDTOResponse.getFirstname());
		assertEquals("lastname", patientFileDTOResponse.getLastname());
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
		assertEquals("D001", patientFileDTOResponse.getReferringDoctorId());
		assertEquals("2000-02-13", patientFileDTOResponse.getDateOfBirth().toString());
	}

	@Test
	public void testFindPatientFileFailurePatientFileDoesNotExist() throws FinderException {
		when(patientFileDAO.findById("P003")).thenReturn(Optional.ofNullable(null));

		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.findPatientFile("P003"));

		verify(patientFileDAO, times(1)).findById("P003");

		assertEquals("patient file not found", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorSuccess() {
		
		patientFileDTO.setReferringDoctorId("D002");
		newDoctor.setId("D002");
		assertEquals("D001", persistentPatientFile.getReferringDoctor().getId());
		
		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.of(persistentPatientFile));
		when(doctorDAO.findById(patientFileDTO.getReferringDoctorId())).thenReturn(Optional.of(newDoctor));
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		patientFileDTOResponse = assertDoesNotThrow(() -> patientFileService.updateReferringDoctor(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(1)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), savedPatientFile.getLastname());
		assertEquals(persistentPatientFile.getSecurityCode(), savedPatientFile.getSecurityCode());
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getPhone(), savedPatientFile.getPhone());
		assertEquals(persistentPatientFile.getEmail(), savedPatientFile.getEmail());
		assertEquals(persistentPatientFile.getAddress().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(persistentPatientFile.getAddress().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(persistentPatientFile.getAddress().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(persistentPatientFile.getAddress().getCountry(), savedPatientFile.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getFirstname(), patientFileDTOResponse.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), patientFileDTOResponse.getLastname());
		assertEquals(null, patientFileDTOResponse.getSecurityCode());
		assertEquals(persistentPatientFile.getId(), patientFileDTOResponse.getId());
		assertEquals(persistentPatientFile.getPhone(), patientFileDTOResponse.getPhone());
		assertEquals(persistentPatientFile.getEmail(), patientFileDTOResponse.getEmail());
		assertEquals(persistentPatientFile.getAddress().getStreet1(), patientFileDTOResponse.getAddressDTO().getStreet1());
		assertEquals(persistentPatientFile.getAddress().getZipcode(), patientFileDTOResponse.getAddressDTO().getZipcode());
		assertEquals(persistentPatientFile.getAddress().getCity(), patientFileDTOResponse.getAddressDTO().getCity());
		assertEquals(persistentPatientFile.getAddress().getCountry(), patientFileDTOResponse.getAddressDTO().getCountry());
		
		// updated - compared to captured saved object
		assertEquals(patientFileDTO.getReferringDoctorId(), savedPatientFile.getReferringDoctor().getId());
		assertNotEquals("D001", savedPatientFile.getReferringDoctor().getId());

		// updated - compared to response DTO object
		assertEquals(patientFileDTO.getReferringDoctorId(), patientFileDTOResponse.getReferringDoctorId());
		assertNotEquals("D001", patientFileDTOResponse.getReferringDoctorId());
	}
	
	@Test
	public void testUpdateReferringDoctorFailurePatientFileDoesNotExist() {
		patientFileDTO.setReferringDoctorId("D002");
		
		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.ofNullable(null));
		
		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.updateReferringDoctor(patientFileDTO));
		
		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(0)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));
		
		assertEquals("patient file not found", ex.getMessage());
	}

	@Test
	public void testUpdateReferringDoctorFailureNewDoctorDoesNotExist() {
		patientFileDTO.setReferringDoctorId("D002");
		
		when(patientFileDAO.findById(patientFileDTO.getId())).thenReturn(Optional.ofNullable(persistentPatientFile));
		when(doctorDAO.findById(patientFileDTO.getReferringDoctorId())).thenReturn(Optional.ofNullable(null));
		
		FinderException ex = assertThrows(FinderException.class, () -> patientFileService.updateReferringDoctor(patientFileDTO));
		
		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(doctorDAO, times(1)).findById(patientFileDTO.getReferringDoctorId());
		verify(patientFileDAO, times(0)).save(any(PatientFile.class));
		
		assertEquals("doctor not found", ex.getMessage());
	}
	
	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound2() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("la")).thenReturn(List.of(patientFile1, patientFile2));
		
		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("la");
		
		verify(patientFileDAO, times(1)).findByIdOrFirstnameOrLastname("la");
		
		assertEquals(2, patientFilesDTO.size());
		
		patientFileDTO1 = patientFilesDTO.get(0);
		patientFileDTO2 = patientFilesDTO.get(1);
		
		assertEquals("ID_1", patientFileDTO1.getId());
		assertEquals("2000-02-13", patientFileDTO1.getDateOfBirth().toString());
		assertEquals("zipcode", patientFileDTO1.getAddressDTO().getZipcode());
		assertEquals("D001", patientFileDTO1.getReferringDoctorId());
		assertEquals("ID_2", patientFileDTO2.getId());
		assertEquals("1995-08-21", patientFileDTO2.getDateOfBirth().toString());
		assertEquals("zipcode", patientFileDTO2.getAddressDTO().getZipcode());
		assertEquals("D001", patientFileDTO2.getReferringDoctorId());
	}

	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound0() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("za")).thenReturn(List.of());
		
		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("za");
		
		verify(patientFileDAO, times(1)).findByIdOrFirstnameOrLastname("za");
		
		assertEquals(0, patientFilesDTO.size());
	}

	@Test
	public void testFindPatientFileByIdOrFirstnameOrLastnameFound0SearchStringIsBlank() {
		when(patientFileDAO.findByIdOrFirstnameOrLastname("")).thenReturn(List.of());
		
		List<PatientFileDTO> patientFilesDTO = patientFileService.findPatientFilesByIdOrFirstnameOrLastname("");
		
		verify(patientFileDAO, times(0)).findByIdOrFirstnameOrLastname("");
		
		assertEquals(0, patientFilesDTO.size());
	}
	
	@Test
	public void testCreateCorrespondanceSuccess() {
		when(correspondanceDAO.save(correspondanceCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		correspondanceDTOResponse = assertDoesNotThrow(() -> patientFileService.createCorrespondance(correspondanceDTO));
		
		verify(correspondanceDAO, times(1)).save(any(Correspondance.class));
		
		savedCorrespondance = correspondanceCaptor.getValue();
		
		assertEquals(correspondanceDTO.getDateUntil(), savedCorrespondance.getDateUntil());
		assertEquals(correspondanceDTO.getDoctorId(), savedCorrespondance.getDoctor().getId());
		assertEquals(correspondanceDTO.getPatientFileId(), savedCorrespondance.getPatientFile().getId());
		
		assertEquals(correspondanceDTO.getDateUntil(), correspondanceDTOResponse.getDateUntil());
		assertEquals(correspondanceDTO.getDoctorId(), correspondanceDTOResponse.getDoctorId());
		assertEquals(correspondanceDTO.getPatientFileId(), correspondanceDTOResponse.getPatientFileId());
	}
	
	@Test
	public void testDeleteCorrespondanceSuccess() {
		
		uuid = UUID.randomUUID();
		
		doNothing().when(correspondanceDAO).deleteById(uuid);
		
		patientFileService.deleteCorrespondance(uuid);
		
		verify(correspondanceDAO, times(1)).deleteById(uuid);
	}

}
