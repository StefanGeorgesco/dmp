package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PatientFileServiceTest {

	@MockBean
	private RNIPPService rnippService;

	@MockBean
	private PatientFileDAO patientFileDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO response;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile persistentPatientFile;

	@Autowired
	private PatientFile savedPatientFile;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<PatientFile> patientFileCaptor = ArgumentCaptor.forClass(PatientFile.class);

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		doctorDTO.setId("D001");
		doctorDTO.setLastname("Smith");
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorDTO(doctorDTO);

		doctor.setId("D001");
		persistentPatientFile.setId(patientFileDTO.getId());
		persistentPatientFile.setFirstname("firstname");
		persistentPatientFile.setLastname("lastname");
		persistentPatientFile.setSecurityCode("securityCode");
		persistentPatientFile.setReferringDoctor(doctor);
	}

	@Test
	public void testCreatePatientFileSuccess() throws CheckException {
		doNothing().when(rnippService).checkPatientData(patientFileDTO);
		when(patientFileDAO.existsById(patientFileDTO.getId())).thenReturn(false);
		when(patientFileDAO.save(patientFileCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		response = assertDoesNotThrow(() -> patientFileService.createPatientFile(patientFileDTO));

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
		assertEquals(patientFileDTO.getReferringDoctorDTO().getId(), savedPatientFile.getReferringDoctor().getId());
		assertEquals(patientFileDTO.getReferringDoctorDTO().getLastname(),
				savedPatientFile.getReferringDoctor().getLastname());

		assertNotNull(response.getSecurityCode());
		assertTrue(response.getSecurityCode().length() >= 10);
		assertTrue(bCryptPasswordEncoder.matches(response.getSecurityCode(), savedPatientFile.getSecurityCode()));
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

		response = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));

		verify(patientFileDAO, times(1)).findById(patientFileDTO.getId());
		verify(patientFileDAO, times(1)).save(any(PatientFile.class));

		savedPatientFile = patientFileCaptor.getValue();
		
		assertEquals(persistentPatientFile.getId(), savedPatientFile.getId());
		assertEquals(persistentPatientFile.getFirstname(), savedPatientFile.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), savedPatientFile.getLastname());
		assertEquals(persistentPatientFile.getSecurityCode(), savedPatientFile.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(),
				savedPatientFile.getReferringDoctor().getId());
		
		assertEquals(patientFileDTO.getId(), savedPatientFile.getId());
		assertEquals(patientFileDTO.getPhone(), savedPatientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), savedPatientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), savedPatientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), savedPatientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), savedPatientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), savedPatientFile.getAddress().getCountry());
		
		assertEquals(persistentPatientFile.getId(), response.getId());
		assertEquals(persistentPatientFile.getFirstname(), response.getFirstname());
		assertEquals(persistentPatientFile.getLastname(), response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals(persistentPatientFile.getReferringDoctor().getId(),
				response.getReferringDoctorDTO().getId());
		
		assertEquals(patientFileDTO.getId(), response.getId());
		assertEquals(patientFileDTO.getPhone(), response.getPhone());
		assertEquals(patientFileDTO.getEmail(), response.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

}
