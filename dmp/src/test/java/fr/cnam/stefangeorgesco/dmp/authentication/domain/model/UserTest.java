package fr.cnam.stefangeorgesco.dmp.authentication.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserTest {

	private static Validator validator;

	@BeforeAll
	public static void setup() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	public void userValidationUserValid() {

		User user = new User();

		user.setId("A");
		user.setUsername("a");
		user.setPassword("1234");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(0, violations.size());
	}

	@Test
	public void userValidationIdInvalidBlank() {

		User user = new User();

		user.setId("");
		user.setUsername("a");
		user.setPassword("1234");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void userValidationUsernameInvalidBlank() {

		User user = new User();

		user.setId("A");
		user.setUsername("");
		user.setPassword("1234");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("username is mandatory", violations.iterator().next().getMessage());

	}
	
	@Test
	public void userValidationPasswordInvalidLessThanFourCharacters() {

		User user = new User();

		user.setId("A");
		user.setUsername("a");
		user.setPassword("123");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("password should at least be 4 characters long", violations.iterator().next().getMessage());

	}
	
	@Test
	public void userValidationIdInvalidNull() {

		User user = new User();

		user.setUsername("a");
		user.setPassword("1234");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("id is mandatory", violations.iterator().next().getMessage());

	}

	@Test
	public void userValidationUsernameInvalidNull() {

		User user = new User();

		user.setId("A");
		user.setPassword("1234");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("username is mandatory", violations.iterator().next().getMessage());

	}
	
	@Test
	public void userValidationPasswordInvalidNull() {

		User user = new User();

		user.setId("A");
		user.setUsername("a");

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertEquals(1, violations.size());
		assertEquals("password is mandatory", violations.iterator().next().getMessage());

	}
	
}
