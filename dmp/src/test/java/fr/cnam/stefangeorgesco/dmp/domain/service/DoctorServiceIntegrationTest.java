package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class DoctorServiceIntegrationTest {
	
	@Autowired
	private SpecialtyDAO specilatyDAO;
	
	@Autowired
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
	private Specialty specialty;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	private List<SpecialtyDTO> specialtyDTOs;

	private List<Specialty> specialties;

	@BeforeEach
	public void setupBeforeEach() {
		specialtyDTO.setId("S001");
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
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);
		
		specialty.setId("S001");
		specialty.setDescription("A specialty");
		specilatyDAO.save(specialty);
		specialties = new ArrayList<Specialty>();
		specialties.add(specialty);
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		doctor.setId("D001");
		doctor.setFirstname("Pierre");
		doctor.setLastname("Martin");
		doctor.setPhone("012345679");
		doctor.setEmail("pierre.martin@docteurs.fr");
		doctor.setSpecialties(specialties);
		doctor.setAddress(address);
		doctor.setSecurityCode("code");
	}
	
	@AfterEach
	public void tearDown() {
		if (doctorDAO.existsById("D001")) {
			doctorDAO.deleteById("D001");
		}
		if (specilatyDAO.existsById("S001")) {
			specilatyDAO.deleteById("S001");
		}
	}
	
	@Test
	public void testCreateDoctorSuccess() {
		
		assertFalse(doctorDAO.existsById("D001"));
		
		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));
		
		assertTrue(doctorDAO.existsById("D001"));
	}
	
	@Test
	public void testCreateDoctorFailureDoctorAlreadyExists() {
		doctorDAO.save(doctor);
		
		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> doctorService.createDoctor(doctorDTO));
		
		assertEquals("doctor already exists", ex.getMessage());	
	}
	
	@Test
	public void testFindDoctorSuccess() {
		doctorDAO.save(doctor);
		
		DoctorDTO doctorDTO = assertDoesNotThrow(() -> doctorService.findDoctor("D001"));
		
		assertEquals("D001", doctorDTO.getId());
		assertEquals("1 Rue Lecourbe", doctorDTO.getAddressDTO().getStreet1());
		assertEquals(1, ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).size());
		assertEquals("S001", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(0).getId());
		assertNull(doctorDTO.getSecurityCode());
	}
	
	@Test
	public void testFindDoctorFailureDoctorDoesNotExist() {
		
		FinderException ex = assertThrows(FinderException.class, () -> doctorService.findDoctor("D001"));
		
		assertEquals("doctor not found", ex.getMessage());
	}
	
}






