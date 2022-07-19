package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PatientFileServiceIntegrationTest {

	@MockBean
	private RNIPPService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private PatientFileService patientFileService;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private PatientFileDTO response;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private PatientFile patientFile;
	
	@Autowired
	private PatientFile savedPatientFile;

	@BeforeEach
	public void setup() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris Cedex 15");
		addressDTO.setCountry("France-");
		doctorDTO.setId("D001");
		patientFileDTO.setId("P002");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorDTO(doctorDTO);

		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris Cedex 15");
		address.setCountry("France-");
		doctor.setId("D001");
		patientFile.setId("P002");
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("code");
		patientFile.setReferringDoctor(doctor);
	}

	@AfterEach
	public void teardown() {
		if (patientFileDAO.existsById("P002")) {
			patientFileDAO.deleteById("P002");
		}
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

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> patientFileService.createPatientFile(patientFileDTO));
		
		assertEquals("patient file already exists", ex.getMessage());
	}
	
	@Test
	public void testUpdatePatientFileSuccess() {
		doctorDTO.setId("D002"); // try to change doctor
		patientFileDTO.setReferringDoctorDTO(doctorDTO);
		patientFileDTO.setId("P001"); // file exists
		
		assertTrue(patientFileDAO.existsById("P001"));
		
		response = assertDoesNotThrow(() -> patientFileService.updatePatientFile(patientFileDTO));
		
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
		assertEquals("P001", response.getId());
		assertEquals("Eric", response.getFirstname());
		assertEquals("Martin", response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals("D001", response.getReferringDoctorDTO().getId());
		
		assertEquals(patientFileDTO.getId(), response.getId());
		// changes in returned object
		assertEquals(patientFileDTO.getPhone(), response.getPhone());
		assertEquals(patientFileDTO.getEmail(), response.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

}
