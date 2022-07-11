package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

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
	
	@Test
	public void testPatientFileDAOExistsById() {
		assertFalse(patientFileDAO.existsById("1"));
		assertFalse(patientFileDAO.existsById("0"));
		assertTrue(patientFileDAO.existsById("2"));
	}
	
	@Test
	public void testPatientFileDAOFindById() {
		
		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById("2");
		
		assertTrue(optionalPatientFile.isPresent());
		
		PatientFile patientFile = optionalPatientFile.get();
		
		assertEquals(patientFile.getFirstname(), "Eric");
		assertEquals(patientFile.getLastname(), "Martin");
		
	}
	
}
