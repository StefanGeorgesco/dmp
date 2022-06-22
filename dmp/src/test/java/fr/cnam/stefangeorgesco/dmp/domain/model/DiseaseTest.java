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
public class DiseaseTest {
	
	private static Validator validator;
	public Disease disease;
	
	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void setupEach() {
		disease = new Disease();
		disease.setId("id");
		disease.setDescription("A disease");
	}
	
	@Test
	public void diseaseValidationDiseaseValid() {
		
		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);
		
		assertEquals(0, violations.size());
	}
	
	@Test
	public void diseaseValidationIdBlank() {
		
		disease.setId("");
		
		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationDescriptionBlank() {
		
		disease.setDescription("");
		
		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);
		
		assertEquals(1, violations.size());
		assertEquals("description is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationIdNull() {
		
		disease.setId(null);
		
		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void diseaseValidationDescriptionNull() {
		
		disease.setDescription(null);
		
		Set<ConstraintViolation<Disease>> violations = validator.validate(disease);
		
		assertEquals(1, violations.size());
		assertEquals("description is mandatory", violations.iterator().next().getMessage());
	}

}
