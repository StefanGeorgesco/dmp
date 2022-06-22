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
public class CorrespondanceTest {

	private static Validator validator;
	private Correspondance correspondance;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		correspondance = new Correspondance();
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		correspondance.setId("id");
		correspondance.setDateUntil(futureDate);
	}

	@Test
	public void correspondanceValidationCorrespondaceValid() {

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(0, violations.size());
	}

	@Test
	public void correspondanceValidationIdBlank() {

		correspondance.setId("");

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationDateNow() {

		correspondance.setDateUntil(now);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationDatePast() {

		correspondance.setDateUntil(pastDate);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correpondance date must be in the future", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationIdNull() {

		correspondance.setId(null);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void correspondanceValidationDateNull() {

		correspondance.setDateUntil(null);

		Set<ConstraintViolation<Correspondance>> violations = validator.validate(correspondance);

		assertEquals(1, violations.size());
		assertEquals("correspondance date is mandatory", violations.iterator().next().getMessage());
	}

}
