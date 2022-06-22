package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class SpecialtyTest {
	
	private static Validator validator;
	public Specialty specialty;
	
	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void setupEach() {
		specialty = new Specialty();
		specialty.setId("id");
		specialty.setDescription("A specialty");
	}
	
	@Test
	public void specialtyValidationSpecialtyValid() {
		
		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);
		
		assertEquals(0, violations.size());
	}
	
	@Test
	public void specialtyValidationIdBlank() {
		
		specialty.setId("");
		
		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationDescriptionBlank() {
		
		specialty.setDescription("");
		
		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);
		
		assertEquals(1, violations.size());
		assertEquals("description is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationIdNull() {
		
		specialty.setId(null);
		
		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void specialtyValidationDescriptionNull() {
		
		specialty.setDescription(null);
		
		Set<ConstraintViolation<Specialty>> violations = validator.validate(specialty);
		
		assertEquals(1, violations.size());
		assertEquals("description is mandatory", violations.iterator().next().getMessage());
	}

}
