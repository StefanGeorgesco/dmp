package fr.cnam.stefangeorgesco.dmp.domain.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorDAOTest {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	DoctorDAO doctorDAO;

	@Autowired
	private Address doctorAddress;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Doctor doctor;

	private List<Specialty> specialties;

	@BeforeEach
	public void setup() {
		specialty.setId("s001");
		specialty.setDescription("A specialty");
		specialtyDAO.save(specialty);
		specialties = new ArrayList<>();
		specialties.add(specialty);
		doctorAddress.setStreet1("street");
		doctorAddress.setCity("city");
		doctorAddress.setZipcode("zip");
		doctorAddress.setCountry("country");
		doctor.setId("doctorId");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode("12345678");
	}

	@Test
	public void testDoctorDAOExistsById() {
		assertTrue(doctorDAO.existsById("1"));
		assertFalse(doctorDAO.existsById("0"));
		assertFalse(doctorDAO.existsById("2"));
	}

	@Test
	public void testDoctorDAOFindById() {
		Optional<Doctor> optionalDoctor = doctorDAO.findById("1");

		assertTrue(optionalDoctor.isPresent());

		Doctor doctor = optionalDoctor.get();

		assertEquals(doctor.getFirstname(), "John");
		assertEquals(doctor.getLastname(), "Smith");
	}

	@Test
	public void testDoctorDAOSave() {
		assertFalse(doctorDAO.existsById("doctorId"));

		doctorDAO.save(doctor);

		assertTrue(doctorDAO.existsById("doctorId"));
	}

	@Test
	public void testDoctorDAOSaveFailureInvalidData() {
		doctor.getSpecialties().clear();

		assertThrows(RuntimeException.class, () -> doctorDAO.save(doctor));
		assertFalse(doctorDAO.existsById("doctorId"));
	}

	@Test
	void testDoctorDAODeleteSuccess() {
		assertTrue(doctorDAO.existsById("3"));
		List<Map<String, Object>> results = jdbc.queryForList(
						"SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='3';");
		assertEquals(2, results.size());

		doctor.setId("3");

		assertDoesNotThrow(() -> doctorDAO.delete(doctor));

		assertFalse(doctorDAO.existsById("3"));
		results = jdbc.queryForList(
				"SELECT * FROM t_doctor_specialty where t_doctor_specialty.doctor_id='3';");
		assertEquals(0, results.size());
	}
	
	@Test
	public void testDoctorDAODeleteFailureDoctorReferringDoctor() {
		assertTrue(doctorDAO.existsById("1"));
		
		doctor.setId("1");
		
		assertThrows(RuntimeException.class, () -> doctorDAO.delete(doctor));
		
		assertTrue(doctorDAO.existsById("1"));
	}

}
