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

import fr.cnam.stefangeorgesco.dmp.domain.model.File;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class FileDAOTest {
	
	@Autowired
	private FileDAO fileDAO;
	
	@Test
	public void testFileDAOExistsById() {
		assertTrue(fileDAO.existsById("1"));
		assertTrue(fileDAO.existsById("2"));
		assertFalse(fileDAO.existsById("0"));
	}
	
	@Test
	public void testFileDAOFindById() {
		
		Optional<File> optionalFile = fileDAO.findById("1");
		
		assertTrue(optionalFile.isPresent());
		
		File file = optionalFile.get();
		
		assertEquals(file.getFirstname(), "John");
		assertEquals(file.getLastname(), "Smith");
		
	}
	
}
