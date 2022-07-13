package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class DoctorServiceTest {

	@MockBean
	private DoctorDAO doctorDAO;
	
	@MockBean
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private SpecialtyDTO specialtyDTO1;

	@Autowired
	private SpecialtyDTO specialtyDTO2;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;
	
	@Autowired
	private Specialty specialty1;
	
	@Autowired
	private Specialty specialty2;

	@Autowired
	private Doctor doctor;

	private Set<SpecialtyDTO> specialtyDTOs;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO1.setId("S001");
		specialtyDTO2.setId("S002");
		specialtyDTOs = new HashSet<SpecialtyDTO>();
		specialtyDTOs.add(specialtyDTO1);
		specialtyDTOs.add(specialtyDTO2);
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		doctorDTO.setId("D001");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);
		
		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");
	}

	@Test
	public void testCreateDoctorSuccess() {
		when(doctorDAO.existsById(doctorDTO.getId())).thenReturn(false);
		when(specialtyDAO.findById(specialtyDTO1.getId())).thenReturn(Optional.of(specialty1));
		when(specialtyDAO.findById(specialtyDTO2.getId())).thenReturn(Optional.of(specialty2));
		when(doctorDAO.save(doctorCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));

		verify(doctorDAO, times(1)).existsById(doctorDTO.getId());
		verify(specialtyDAO, times(2)).findById(any(String.class));
		verify(doctorDAO, times(1)).save(any(Doctor.class));

		doctor = doctorCaptor.getValue();

		assertEquals(doctorDTO.getId(), doctor.getId());
		assertEquals(doctorDTO.getFirstname(), doctor.getFirstname());
		assertEquals(doctorDTO.getPhone(), doctor.getPhone());
		assertEquals(doctorDTO.getEmail(), doctor.getEmail());
		assertEquals(doctorDTO.getSpecialtiesDTO().size(), doctor.getSpecialties().size());
		assertEquals(doctorDTO.getSpecialtiesDTO().iterator().next().getDescription(),
				doctor.getSpecialties().iterator().next().getDescription());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), doctor.getAddress().getZipcode());
		assertTrue(bCryptPasswordEncoder.matches(doctorDTO.getSecurityCode(), doctor.getSecurityCode()));
		
		assertNotNull(doctorDTO.getSecurityCode());
		assertTrue(doctorDTO.getSecurityCode().length() >= 10);
		assertEquals(2, doctorDTO.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> it = doctorDTO.getSpecialtiesDTO().iterator();
		SpecialtyDTO specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription()) ||
				"S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
		specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription()) ||
				"S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
	}

	@Test
	public void testCreateDoctorFailureDoctorAlreadyExists() {
		when(doctorDAO.existsById(doctorDTO.getId())).thenReturn(true);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> doctorService.createDoctor(doctorDTO));

		verify(doctorDAO, times(1)).existsById(doctorDTO.getId());
		verify(doctorDAO, times(0)).save(any(Doctor.class));

		assertEquals("doctor already exists", ex.getMessage());
	}

}
