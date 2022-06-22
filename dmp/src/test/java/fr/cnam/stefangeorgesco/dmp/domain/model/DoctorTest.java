package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class DoctorTest {

	private static Validator validator;
	private Doctor doctor;
	private Address address;
	private List<Specialty> specialties;
	private Specialty specialty;
	private User user;

	@BeforeAll
	public static void setupAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void setupEach() {
		specialty = new Specialty();
		specialty.setId("specialtyId");
		specialty.setDescription("A specialty");
		
		specialties = new ArrayList<>();		
		specialties.add(specialty);
		
		address = new Address();
		address.setStreet1("street");
		address.setCity("city");
		address.setZipcode("zip");
		address.setCountry("country");
		
		doctor = new Doctor();
		doctor.setId("id");
		doctor.setFirstname("firstname");
		doctor.setLastname("lastname");
		doctor.setPhone("0123456789");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(address);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode("12345678");
		
		user = new User();
		user.setId(doctor.getId());
		user.setSecurityCode(doctor.getSecurityCode());
		
	}
	
	@Test
	public void doctorValidationDoctorVAlid() {
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(0, violations.size());
	}
	
	@Test
	public void doctorValidationIdInvalidBlank() {
		
		doctor.setId("");
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationFirstnameInvalidBlank() {
		
		doctor.setFirstname("");
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("firstname is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationLastnameInvalidBlank() {
		
		doctor.setLastname("");
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("lastname is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationPhoneInvalidBlank() {
		
		doctor.setPhone("");
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("phone is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationEmailInvalidFormat() {
		
		doctor.setEmail("email");
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("email must be given and respect format", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationIdInvalidNull() {
		
		doctor.setId(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationFirstnameInvalidNull() {
		
		doctor.setFirstname(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("firstname is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationLastnameInvalidNull() {
		
		doctor.setLastname(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("lastname is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationPhoneInvalidNull() {
		
		doctor.setPhone(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("phone is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationEmailInvalidNull() {
		
		doctor.setEmail(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("email is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationAddressInvalidNull() {
		
		doctor.setAddress(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("address is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationSpecialtiesInvalidNull() {
		
		doctor.setSpecialties(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("specialties are mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationSpecialtiesInvalidEmpty() {
		
		doctor.getSpecialties().clear();
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("doctor must have at least one specialty", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationSpecialtiesInvalidFirstSpecialtyIdNull() {
		
		doctor.getSpecialties().iterator().next().setId(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void doctorValidationAddressInvalidStreet1Null() {
		
		doctor.getAddress().setStreet1(null);
		
		Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);
		
		assertEquals(1, violations.size());
		assertEquals("invalid street", violations.iterator().next().getMessage());

	}
	
	@Test
	public void checkUserDataMatchDoesNotThrowException() {
		
		assertDoesNotThrow(() -> doctor.checkUserData(user));
		
	}

	@Test
	public void checkUserNullUserThrowsCheckException() {
		
		user = null;
		
		CheckException exception = assertThrows(CheckException.class,() -> doctor.checkUserData(user));
		assertEquals("tried to check null user", exception.getMessage());
		
	}
	
	@Test
	public void checkUserNullUserIdThrowsCheckException() {
		
		user.setId(null);
		
		CheckException exception = assertThrows(CheckException.class,() -> doctor.checkUserData(user));
		assertEquals("tried to check user with null id", exception.getMessage());
		
	}
	
	@Test
	public void checkUserNullSecurityCodeThrowsCheckException() {
		
		user.setSecurityCode(null);
		
		CheckException exception = assertThrows(CheckException.class,() -> doctor.checkUserData(user));
		assertEquals("tried to check user with null security code", exception.getMessage());
		
	}

	@Test
	public void checkUserDifferentUserIdThrowsCheckException() {
		
		user.setId("userId");
		
		CheckException exception = assertThrows(CheckException.class,() -> doctor.checkUserData(user));
		assertEquals("data did not match", exception.getMessage());
		
	}
	
	@Test
	public void checkUserDifferentSecurityCodeThrowsCheckException() {
		
		user.setSecurityCode("01234567");
		
		CheckException exception = assertThrows(CheckException.class,() -> doctor.checkUserData(user));
		assertEquals("data did not match", exception.getMessage());
		
	}
	
}
