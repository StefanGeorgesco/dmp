package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

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

import fr.cnam.stefangeorgesco.dmp.domain.model.Address;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class AddressTest {

	private static Validator validator;
	private Address address;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void setupEach() {
		address = new Address();
		address.setStreet1("street");
		address.setCity("city");
		address.setZipcode("zip");
		address.setCountry("country");
	}

	@Test
	public void AddressValidationAddressValid() {

		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(0, violations.size());

	}

	@Test
	public void AddressValidationStreet1InvalidBlank() {

		address.setStreet1("");

		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid street", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationCityInvalidBlank() {

		address.setCity("");

		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid city", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationZipcodeInvalidBlank() {

		address.setZipcode("");

		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid zipcode", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationCountryInvalidBlank() {

		address.setCountry("");

		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid country", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationStreet1InvalidNull() {

		address.setStreet1(null);
		
		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid street", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationCityInvalidNull() {

		address.setCity(null);
		
		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid city", violations.iterator().next().getMessage());
	}

	@Test
	public void AddressValidationZipcodeInvalidNull() {

		address.setZipcode(null);
		
		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid zipcode", violations.iterator().next().getMessage());

	}

	@Test
	public void AddressValidationCountryInvalidNull() {

		address.setCountry(null);
		
		Set<ConstraintViolation<Address>> violations = validator.validate(address);

		assertEquals(1, violations.size());
		assertEquals("invalid country", violations.iterator().next().getMessage());

	}

}