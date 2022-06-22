package fr.cnam.stefangeorgesco.dmp.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class MailTest {

	private static Validator validator;
	private Mail mail;
	private LocalDate now;
	private LocalDate futureDate;
	private LocalDate pastDate;
	private Address doctorAddress;
	private List<Specialty> specialties;
	private Specialty specialty;
	private Doctor doctor;

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
		
		doctorAddress = new Address();
		doctorAddress.setStreet1("street_doctor");
		doctorAddress.setCity("city_doctor");
		doctorAddress.setZipcode("zip_doctor");
		doctorAddress.setCountry("country_doctor");
		
		doctor = new Doctor();
		doctor.setId("doctorId");
		doctor.setFirstname("firstname_doctor");
		doctor.setLastname("lastname_doctor");
		doctor.setPhone("1111111111");
		doctor.setEmail("doctor@doctors.com");
		doctor.setAddress(doctorAddress);
		doctor.setSpecialties(specialties);
		doctor.setSecurityCode("99999999");
		
		mail = new Mail();
		now = LocalDate.now();
		pastDate = now.minusDays(1);
		futureDate = now.plusDays(1);
		mail.setDate(now);
		mail.setText("mail text");
		mail.setTo(doctor);
	}

	@Test
	public void mailValidationMailValidDateNow() {

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(0, violations.size());
	}

	@Test
	public void mailValidationMailValidDatePast() {

		mail.setDate(pastDate);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(0, violations.size());
	}

	@Test
	public void mailValidationInvalidDateFuture() {

		mail.setDate(futureDate);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("patient file item date must be in the past or today", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidTextBlank() {

		mail.setText("");

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("mail text is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidDateNull() {

		mail.setDate(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("patient file date is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidTextNull() {

		mail.setText(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("mail text is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidRecipientDoctorNull() {

		mail.setTo(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("recipient doctor is mandatory", violations.iterator().next().getMessage());
	}

	@Test
	public void mailValidationInvalidRecipientDoctorIdNull() {

		mail.getTo().setId(null);

		Set<ConstraintViolation<Mail>> violations = validator.validate(mail);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());
	}

}
