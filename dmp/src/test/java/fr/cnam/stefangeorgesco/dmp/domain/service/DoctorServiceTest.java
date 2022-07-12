package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

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
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class DoctorServiceTest {

	@MockBean
	private DoctorDAO doctorDAO;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private SpecialtyDTO specialtyDTO;

	@Autowired
	private AddressDTO addressDTO;

	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private Doctor doctor;

	private List<SpecialtyDTO> specialtyDTOs;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO.setId("s001");
		specialtyDTO.setDescription("A specialty");
		specialtyDTOs = new ArrayList<SpecialtyDTO>();
		specialtyDTOs.add(specialtyDTO);
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		doctorDTO.setId("D001");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtyDTOs(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);
	}

	@Test
	public void testCreateDoctorSuccess() {
		when(doctorDAO.existsById(doctorDTO.getId())).thenReturn(false);
		when(doctorDAO.save(doctorCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));

		verify(doctorDAO, times(1)).existsById(doctorDTO.getId());
		verify(doctorDAO, times(1)).save(any(Doctor.class));

		doctor = doctorCaptor.getValue();

		assertEquals(doctorDTO.getId(), doctor.getId());
		assertEquals(doctorDTO.getFirstname(), doctor.getFirstname());
		assertEquals(doctorDTO.getPhone(), doctor.getPhone());
		assertEquals(doctorDTO.getEmail(), doctor.getEmail());
		assertEquals(doctorDTO.getSpecialtyDTOs().size(), doctor.getSpecialties().size());
		assertEquals(doctorDTO.getSpecialtyDTOs().iterator().next().getDescription(),
				doctor.getSpecialties().iterator().next().getDescription());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), doctor.getAddress().getZipcode());
		assertNotNull(doctorDTO.getSecurityCode());
		assertTrue(doctorDTO.getSecurityCode().length() >= 10);
		assertTrue(bCryptPasswordEncoder.matches(doctorDTO.getSecurityCode(), doctor.getSecurityCode()));
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
