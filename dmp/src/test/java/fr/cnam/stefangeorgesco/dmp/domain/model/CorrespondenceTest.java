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
public class CorrespondenceTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;
	
	@Autowired
	private Correspondence correspondence;
	
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
		correspondence.setDateUntil(futureDate);
		correspondence.setDoctor(doctor);
		correspondence.setPatientFile(patientFile);
	}

	@Test
	public void correspondenceValidationCorrespondaceValid() {

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(0, violations.size());
	}

	@Test
	public void correspondenceValidationInvalidDateNow() {

		correspondence.setDateUntil(now);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondenceValidationInvalidDatePast() {

		correspondence.setDateUntil(pastDate);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondenceValidationInvalidDateNull() {

		correspondence.setDateUntil(null);

		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("correspondence date is mandatory", violations.iterator().next().getMessage());
	}
	
	@Test
	public void correspondenceValidationInvalidDoctorNull() {
		
		correspondence.setDoctor(null);
		
		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("doctor is mandatory", violations.iterator().next().getMessage());

	}
	
	@Test
	public void correspondenceValidationInvalidPatientFileNull() {
		
		correspondence.setPatientFile(null);
		
		Set<ConstraintViolation<Correspondence>> violations = validator.validate(correspondence);

		assertEquals(1, violations.size());
		assertEquals("patient file is mandatory", violations.iterator().next().getMessage());

	}
	
}
