package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PatientFileDAOTest {
	
	@Autowired
	private PatientFileDAO patientFileDAO;
	
	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;
	
	@Autowired
	private PatientFile patientFile;

	@BeforeEach
	public void setup() {
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
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
	public void testPatientFileDAOExistsById() {
		assertFalse(patientFileDAO.existsById("P002"));
		assertFalse(patientFileDAO.existsById("P003"));
		assertTrue(patientFileDAO.existsById("P001"));
	}
	
	@Test
	public void testPatientFileDAOFindById() {
		
		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById("P001");
		
		assertTrue(optionalPatientFile.isPresent());
		
		PatientFile patientFile = optionalPatientFile.get();
		
		assertEquals(patientFile.getFirstname(), "Eric");
		assertEquals(patientFile.getLastname(), "Martin");
		assertEquals("D001", patientFile.getReferringDoctor().getId());
	}
	
	@Test
	public void testPatientFileDAOSaveCreateSuccess() {
		assertFalse(patientFileDAO.existsById("P002"));
		
		patientFileDAO.save(patientFile);
		
		assertTrue(patientFileDAO.existsById("P002"));
	}
	
	@Test
	public void testPatientFileDAOSaveCreateFailureInvalidData() {
		patientFile.getAddress().setCity(null);
		
		assertThrows(RuntimeException.class, () -> patientFileDAO.save(patientFile));
		
		assertFalse(patientFileDAO.existsById("P002"));
	}
	
	@Test
	public void testPatientFileDAOSaveCreateFailureDoctorDoesNotExist() {
		patientFile.getReferringDoctor().setId("D003");
		
		assertThrows(RuntimeException.class, () -> patientFileDAO.save(patientFile));
		
		assertFalse(patientFileDAO.existsById("P002"));
	}
	
	@Test
	public void testPatientFileDAOSaveUpdateSuccess() {
		
		PatientFile patientFile = patientFileDAO.findById("P001").get();
		
		patientFile.setEmail("mail@mail.com");
		
		patientFileDAO.save(patientFile);
		
		patientFile = patientFileDAO.findById("P001").get();
		
		assertEquals("mail@mail.com", patientFile.getEmail());	
	}
	
}
