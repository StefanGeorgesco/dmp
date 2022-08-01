package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Sql(scripts = "/sql/create-correspondances.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-correspondances.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
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
		
		private long count;
		
		private UUID uuid;
		
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
		
		@Test
		public void testCorrespondanceDAOSaveCreateSuccess() {
			
			count = correspondanceDAO.count();
			
			assertDoesNotThrow(() -> correspondanceDAO.save(correspondance));
			
			assertEquals(count + 1, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAOSaveCreateFailurePatientFileDoesNotExist() {
			
			patientFile.setId("P002");
			
			count = correspondanceDAO.count();
			
			assertThrows(RuntimeException.class , () -> correspondanceDAO.save(correspondance));
			
			assertEquals(count, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAOSaveCreateFailureDoctorDoesNotExist() {
			
			correspondingDoctor.setId("D003");
			
			count = correspondanceDAO.count();
			
			assertThrows(RuntimeException.class , () -> correspondanceDAO.save(correspondance));
			
			assertEquals(count, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAODeleteByIdSuccess() {
			
			uuid = UUID.fromString("3d80bbeb-997e-4354-82d3-68cea80256d6");
			
			count = correspondanceDAO.count();
			
			assertTrue(correspondanceDAO.existsById(uuid));
			
			correspondanceDAO.deleteById(uuid);
			
			assertFalse(correspondanceDAO.existsById(uuid));
			
			assertEquals(count - 1, correspondanceDAO.count());
		}
		
		@Test
		public void testCorrespondanceDAOFindByPatientFileIdFound3() {
			
			List<Correspondance> correspondanceList = new ArrayList<>();
			
			Iterable<Correspondance> correspondances = correspondanceDAO.findByPatientFileId("P001");
			
			correspondances.forEach(correspondanceList::add);
			
			assertEquals(3, correspondanceList.size());
			assertEquals("2023-05-02", correspondanceList.get(0).getDateUntil().toString());
			assertEquals("e1eb3425-d257-4c5e-8600-b125731c458c", correspondanceList.get(1).getId().toString());
			assertEquals("D011", correspondanceList.get(2).getDoctor().getId());
		}
		
		@Test
		public void testCorrespondanceDAOFindByPatientFileIdFound0() {
			
			List<Correspondance> correspondanceList = new ArrayList<>();
			
			Iterable<Correspondance> correspondances = correspondanceDAO.findByPatientFileId("P055");
			
			correspondances.forEach(correspondanceList::add);
			
			assertEquals(0, correspondanceList.size());
		}
}
