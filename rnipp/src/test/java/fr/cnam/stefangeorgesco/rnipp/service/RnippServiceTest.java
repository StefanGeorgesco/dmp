package fr.cnam.stefangeorgesco.rnipp.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.model.RnippRecord;
import fr.cnam.stefangeorgesco.rnipp.repository.RnippRecordRepository;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class RnippServiceTest {
	
	@MockBean
	private RnippRecordRepository rnippRecordRepository;
	
	@Autowired
	private RnippRecord rnippRecord;
	
	@Autowired
	private RnippRecordDto rnippRecordDto;
	
	@Autowired
	private RnippService rnippService;
	
	private String id;
	
	@BeforeEach
	public void setup()
	{
		id = "P001";
		
		rnippRecord.setId(id);
		rnippRecord.setFirstname("Alain");
		rnippRecord.setLastname("Durand");
		rnippRecord.setDateOfBirth(LocalDate.of(1975, 5, 24));
		
		rnippRecordDto.setId(id);
		rnippRecordDto.setFirstname("Alain");
		rnippRecordDto.setLastname("Durand");
		rnippRecordDto.setDateOfBirth(LocalDate.of(1975, 5, 24));
	}
	
	@Test
	public void testCheckDataSuccess() {
		
		when(rnippRecordRepository.findById(id)).thenReturn(Optional.of(rnippRecord));		
		assertTrue(rnippService.checkData(rnippRecordDto));		
		verify(rnippRecordRepository, times(1)).findById(id);
	}
	
	@Test
	public void testCheckDataFailureRecordNotFound() {
		
		when(rnippRecordRepository.findById(id)).thenReturn(Optional.ofNullable(null));		
		assertFalse(rnippService.checkData(rnippRecordDto));		
		verify(rnippRecordRepository, times(1)).findById(id);
	}
	
	@Test
	public void testCheckDataFailureRecordFoundFirstNameDifferent() {
		
		rnippRecord.setFirstname("Marc");
		
		when(rnippRecordRepository.findById(id)).thenReturn(Optional.of(rnippRecord));		
		assertFalse(rnippService.checkData(rnippRecordDto));		
		verify(rnippRecordRepository, times(1)).findById(id);
	}
	
	@Test
	public void testCheckDataFailureRecordFoundLastNameDifferent() {
		
		rnippRecord.setLastname("Dupont");
		
		when(rnippRecordRepository.findById(id)).thenReturn(Optional.of(rnippRecord));		
		assertFalse(rnippService.checkData(rnippRecordDto));		
		verify(rnippRecordRepository, times(1)).findById(id);
	}
	
	@Test
	public void testCheckDataFailureRecordFoundDateOfBirthDifferent() {
		
		rnippRecord.setDateOfBirth(LocalDate.of(1999, 11, 6));
		
		when(rnippRecordRepository.findById(id)).thenReturn(Optional.of(rnippRecord));		
		assertFalse(rnippService.checkData(rnippRecordDto));		
		verify(rnippRecordRepository, times(1)).findById(id);
	}
}
