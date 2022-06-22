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
public class DiagnosisTest {

	private static Validator validator;
	private Diagnosis diagnosis;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;
	private Disease disease;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		disease = new Disease();
		disease.setId("diseaseId");
		disease.setDescription("A disease");
		diagnosis = new Diagnosis();
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		diagnosis.setDate(now);
		diagnosis.setDisease(disease);
	}
	
	@Test
	public void diagnosisValidationSymptomValidDateNow() {

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(0, violations.size());
	}

	@Test
	public void diagnosisValidationSymptomValidDatePast() {

		diagnosis.setDate(pastDate);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(0, violations.size());
	}

	@Test
	public void diagnosisValidationInvalidDateFuture() {

		diagnosis.setDate(futureDate);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("patient file item date must be in the past or today", violations.iterator().next().getMessage());
	}
	
	@Test
	public void diagnosisValidationInvalidDateNull() {

		diagnosis.setDate(null);

		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("patient file date is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void diagnosisValidationInvalidDiseaseNull() {
		
		diagnosis.setDisease(null);
		
		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("disease is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void diagnosisValidationInvalidDiseaseInvalid() {
		
		diagnosis.getDisease().setId(null);
		
		Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosis);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

}
