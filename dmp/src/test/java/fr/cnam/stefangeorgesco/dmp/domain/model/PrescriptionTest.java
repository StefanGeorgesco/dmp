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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PrescriptionTest {

	private static Validator validator;
	private Prescription prescription;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		prescription = new Prescription();
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		prescription.setDate(now);
		prescription.setDescription("Prescription description");
	}
	
	@Test
	public void prescriptionValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(0, violations.size());
	}

	@Test
	public void prescriptionValidationSymptomValidDatePast() {

		prescription.setDate(pastDate);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(0, violations.size());
	}

	@Test
	public void prescriptionValidationInvalidDateFuture() {

		prescription.setDate(futureDate);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("patient file item date must be in the past or today", violations.iterator().next().getMessage());
	}
	
	@Test
	public void prescriptionValidationInvalidTextBlank() {
		
		prescription.setDescription("");
		
		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("prescription description is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void prescriptionValidationInvalidDateNull() {

		prescription.setDate(null);

		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("patient file date is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void prescriptionValidationInvalidTextNull() {
		
		prescription.setDescription(null);
		
		Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);

		assertEquals(1, violations.size());
		assertEquals("prescription description is mandatory", violations.iterator().next().getMessage());
	}

}
