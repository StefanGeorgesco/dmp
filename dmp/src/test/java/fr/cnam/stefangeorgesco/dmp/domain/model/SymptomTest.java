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
public class SymptomTest {

	private static Validator validator;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;
	
	@Autowired
	private Symptom symptom;
	
	@Autowired
	private Doctor authoringDoctor;
	
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
		symptom.setDate(now);
		symptom.setDescription("Symptom description");
		symptom.setAuthoringDoctor(authoringDoctor);
		symptom.setPatientFile(patientFile);
	}
	
	@Test
	public void symptomValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(0, violations.size());
	}

	@Test
	public void symptomValidationSymptomValidDatePast() {

		symptom.setDate(pastDate);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(0, violations.size());
	}

	@Test
	public void symptomValidationInvalidDateFuture() {

		symptom.setDate(futureDate);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("patient file item date must be in the past or today", violations.iterator().next().getMessage());
	}
	
	@Test
	public void symptomValidationInvalidDesciptionBlank() {
		
		symptom.setDescription("");
		
		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("symptom description is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void symptomValidationInvalidDateNull() {

		symptom.setDate(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("patient file date is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidAuthoringDoctorNull() {

		symptom.setAuthoringDoctor(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("authoring doctor is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void actValidationInvalidPatientFileNull() {

		symptom.setPatientFile(null);

		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("patient file is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void symptomValidationInvalidDesciptionNull() {
		
		symptom.setDescription(null);
		
		Set<ConstraintViolation<Symptom>> violations = validator.validate(symptom);

		assertEquals(1, violations.size());
		assertEquals("symptom description is mandatory", violations.iterator().next().getMessage());
	}

}
