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

import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-doctors.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-doctors.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class DoctorDAOTest {
	
	@Autowired
	private DoctorDAO doctorDAO;
	
	@Test
	public void testDoctorDAOExistsById() {
		assertTrue(doctorDAO.existsById("1"));
		assertFalse(doctorDAO.existsById("0"));
	}
	
	@Test
	public void testDoctorDAOFindById() {
		
		Optional<Doctor> optionalDoctor = doctorDAO.findById("1");
		
		assertTrue(optionalDoctor.isPresent());
		
		Doctor doctor = optionalDoctor.get();
		
		assertEquals(doctor.getFirstname(), "John");
		assertEquals(doctor.getLastname(), "Smith");
		
	}
	
}
