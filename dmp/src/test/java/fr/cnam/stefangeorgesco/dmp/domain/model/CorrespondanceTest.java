package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CorrespondanceTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;
	
	@Autowired
	private Correspondance correspondance;
	
	@Autowired
	private Doctor doctor;
	
	@Autowired
	private PatientFile patientFile;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		correspondance.setDateUntil(futureDate);
		correspondance.setDoctor(doctor);
		correspondance.setPatientFile(patientFile);
	}

	@Test
	public void correspondanceValidationCorrespondaceValid() {

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(0, violations.size());
	}

	@Test
	public void correspondanceValidationInvalidDateNow() {

		correspondance.setDateUntil(now);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationInvalidDatePast() {

		correspondance.setDateUntil(pastDate);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationInvalidDateNull() {

		correspondance.setDateUntil(null);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correspondance date is mandatory", violations.iterator().next().getMessage());
	}
	
	@Test
	public void correspondanceValidationInvalidDoctorNull() {
		
		correspondance.setDoctor(null);
		
		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("doctor is mandatory", violations.iterator().next().getMessage());

	}
	
	@Test
	public void correspondanceValidationInvalidPatientFileNull() {
		
		correspondance.setPatientFile(null);
		
		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("patient file is mandatory", violations.iterator().next().getMessage());

	}
	
}
