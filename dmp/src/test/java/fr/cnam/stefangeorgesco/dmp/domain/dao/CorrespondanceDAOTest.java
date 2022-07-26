package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class CorrespondanceDAOTest {
	
	  @Autowired
	  private CorrespondanceDAO correspondanceDAO;

		@Autowired
		private Address address;

		@Autowired
		private Doctor referringDoctor;
		
		@Autowired
		private Doctor correspondingDoctor;
		
		@Autowired
		private PatientFile patientFile;
		
		@Autowired
		private Correspondance correspondance;

		private LocalDate date;
		
		@BeforeEach
		public void setup() {
			address.setStreet1("1 Rue Lecourbe");
			address.setZipcode("75015");
			address.setCity("Paris");
			address.setCountry("France");
			
			referringDoctor.setId("D001");
			
			patientFile.setId("P001");
			patientFile.setFirstname("Patrick");
			patientFile.setLastname("Dubois");
			patientFile.setDateOfBirth(LocalDate.of(2000, 2, 13));
			patientFile.setPhone("9876543210");
			patientFile.setEmail("patrick.dubois@mail.fr");
			patientFile.setAddress(address);
			patientFile.setSecurityCode("code");
			patientFile.setReferringDoctor(referringDoctor);
			
			correspondingDoctor.setId("D002");
			
			date = LocalDate.now().plusDays(1);
			
			correspondance.setDateUntil(date);
			correspondance.setDoctor(correspondingDoctor);
			correspondance.setPatientFile(patientFile);
		}
		
		@AfterEach
		public void tearDown() {
			correspondanceDAO.deleteAll();
		}
		
		@Test
		public void testCorrespondanceDAOSaveCreateSuccess() {
			
			assertEquals(0, correspondanceDAO.count());
			
			assertDoesNotThrow(() -> correspondanceDAO.save(correspondance));
			
			assertEquals(1, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAOSaveCreateFailurePatientFileDoesNotExist() {
			
			patientFile.setId("P002");
			
			assertEquals(0, correspondanceDAO.count());
			
			assertThrows(RuntimeException.class , () -> correspondanceDAO.save(correspondance));
			
			assertEquals(0, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAOSaveCreateFailureDoctorDoesNotExist() {
			
			correspondingDoctor.setId("D003");
			
			assertEquals(0, correspondanceDAO.count());
			
			assertThrows(RuntimeException.class , () -> correspondanceDAO.save(correspondance));
			
			assertEquals(0, correspondanceDAO.count());
		}
}
