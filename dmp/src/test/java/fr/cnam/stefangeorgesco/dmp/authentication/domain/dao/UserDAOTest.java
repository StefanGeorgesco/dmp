package fr.cnam.stefangeorgesco.dmp.authentication.domain.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({
    @Sql(scripts = "/sql/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "/sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserDAOTest {
	
	@Autowired
	UserDAO userDAO;
	
	@Test
	public void testUserDAOExistsById() {
		assertTrue(userDAO.existsById("1"));
		assertFalse(userDAO.existsById("0"));
	}
	
	@Test
	public void testUserDAOSave() {
		assertFalse(userDAO.existsById("0"));
		
		User user = new User();
		user.setId("0");
		user.setUsername("user0");
		user.setRole("ROLE");
		user.setPassword("passwd");
		user.setSecurityCode("");
		
		userDAO.save(user);
		
		assertTrue(userDAO.existsById("0"));
		
	}
}
