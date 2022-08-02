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

import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-specialties.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-specialties.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class SpecialtyDAOTest {
	
	@Autowired
	private SpecialtyDAO specialtyDAO;
	
	@Autowired
	private Specialty specialty;
	
	@Test
	public void testSpecialtyDAOFindByIdSuccess() {
		
		Optional<Specialty> optionalSpecialty = specialtyDAO.findById("S024");
		
		assertTrue(optionalSpecialty.isPresent());
		
		specialty = optionalSpecialty.get();
		
		assertEquals("S024", specialty.getId());
		assertEquals("médecine générale", specialty.getDescription());
	}

	@Test
	public void testSpecialtyDAOFindByIdNotFound() {
		
		Optional<Specialty> optionalSpecialty = specialtyDAO.findById("S124");
		
		assertFalse(optionalSpecialty.isPresent());
	}

}
