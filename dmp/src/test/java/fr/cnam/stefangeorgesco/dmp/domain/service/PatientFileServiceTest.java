package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	private PatientFile patientFile;

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

		patientFile = patientFileCaptor.getValue();

		assertEquals(patientFileDTO.getId(), patientFile.getId());
		assertEquals(patientFileDTO.getFirstname(), patientFile.getFirstname());
		assertEquals(patientFileDTO.getLastname(), patientFile.getLastname());
		assertEquals(patientFileDTO.getPhone(), patientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFile.getAddress().getCountry());
		assertEquals(patientFileDTO.getReferringDoctorDTO().getId(), patientFile.getReferringDoctor().getId());
		assertEquals(patientFileDTO.getReferringDoctorDTO().getLastname(),
				patientFile.getReferringDoctor().getLastname());

		assertNotNull(response.getSecurityCode());
		assertTrue(response.getSecurityCode().length() >= 10);
		assertTrue(bCryptPasswordEncoder.matches(response.getSecurityCode(), patientFile.getSecurityCode()));
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

}
