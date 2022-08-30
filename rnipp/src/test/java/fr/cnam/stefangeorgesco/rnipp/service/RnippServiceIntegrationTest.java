package fr.cnam.stefangeorgesco.rnipp.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-records.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-records.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class RnippServiceIntegrationTest {

	@Autowired
	private RnippRecordDto rnippRecordDto;

	@Autowired
	private RnippService rnippService;

	@BeforeEach
	public void setup() {
		rnippRecordDto.setId("P001");
		rnippRecordDto.setFirstname("Eric");
		rnippRecordDto.setLastname("Martin");
		rnippRecordDto.setDateOfBirth(LocalDate.of(1995, 5, 15));
	}

	@Test
	public void testCheckDataSuccess() {
		assertTrue(rnippService.checkData(rnippRecordDto));
	}

	@Test
	public void testCheckDataFailureRecordNotFound() {
		rnippRecordDto.setId("P01");
		assertFalse(rnippService.checkData(rnippRecordDto));
	}
	
	@Test
	public void testCheckDataFailureRecordFoundFirstNameDifferent() {
		rnippRecordDto.setFirstname("Alain");
		assertFalse(rnippService.checkData(rnippRecordDto));
	}
	
	@Test
	public void testCheckDataFailureRecordFoundLastNameDifferent() {
		rnippRecordDto.setLastname("Durand");
		assertFalse(rnippService.checkData(rnippRecordDto));
	}
	
	@Test
	public void testCheckDataFailureRecordFoundDateOfBirthDifferent() {
		rnippRecordDto.setDateOfBirth(LocalDate.of(1975, 5, 24));
		assertFalse(rnippService.checkData(rnippRecordDto));
	}
}
