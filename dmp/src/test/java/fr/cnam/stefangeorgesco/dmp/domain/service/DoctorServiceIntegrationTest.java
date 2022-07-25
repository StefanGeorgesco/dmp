package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@SqlGroup({ @Sql(scripts = "/sql/create-files.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "/sql/delete-files.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class DoctorServiceIntegrationTest {

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
	private DoctorDTO response;

	@Autowired
	private Specialty specialty;

	@Autowired
	private Address address;

	@Autowired
	private Doctor doctor;

	@Autowired
	private Doctor savedDoctor;

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
		doctorDTO.setId("D003");
		doctorDTO.setFirstname("Pierre");
		doctorDTO.setLastname("Martin");
		doctorDTO.setPhone("012345679");
		doctorDTO.setEmail("pierre.martin@docteurs.fr");
		doctorDTO.setSpecialtiesDTO(specialtyDTOs);
		doctorDTO.setAddressDTO(addressDTO);

		specialty.setId("S001");
		specialty.setDescription("A specialty");
		specialties = new ArrayList<Specialty>();
		specialties.add(specialty);
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		doctor.setId("D003");
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
		if (doctorDAO.existsById("D003")) {
			doctorDAO.deleteById("D003");
		}
	}

	@Test
	public void testCreateDoctorSuccess() {

		assertFalse(doctorDAO.existsById("D003"));

		assertDoesNotThrow(() -> doctorService.createDoctor(doctorDTO));

		assertTrue(doctorDAO.existsById("D003"));
	}

	@Test
	public void testCreateDoctorFailureDoctorAlreadyExists() {
		doctorDAO.save(doctor);

		DuplicateKeyException ex = assertThrows(DuplicateKeyException.class,
				() -> doctorService.createDoctor(doctorDTO));

		assertEquals("doctor already exists", ex.getMessage());
	}

	@Test
	public void testUpdateDoctorSuccess() {
		doctorDTO.setId("D001"); // file exists

		assertTrue(doctorDAO.existsById("D001"));

		response = assertDoesNotThrow(() -> doctorService.updateDoctor(doctorDTO));

		savedDoctor = doctorDAO.findById("D001").get();

		// no change in saved object
		assertEquals("D001", savedDoctor.getId());
		assertEquals("John", savedDoctor.getFirstname());
		assertEquals("Smith", savedDoctor.getLastname());
		assertEquals("45", savedDoctor.getSecurityCode());
		assertEquals(2, savedDoctor.getSpecialties().size());
		Iterator<Specialty> itSavedSp = savedDoctor.getSpecialties().iterator();
		Specialty savedSp = itSavedSp.next();
		assertEquals("S001", savedSp.getId());
		assertEquals("Specialty 1", savedSp.getDescription());
		savedSp = itSavedSp.next();
		assertEquals("S002", savedSp.getId());
		assertEquals("Specialty 2", savedSp.getDescription());

		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		// changes in saved object
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), savedDoctor.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), savedDoctor.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), savedDoctor.getAddress().getCountry());

		// no change in returned object (except null securityCode)
		assertEquals("D001", response.getId());
		assertEquals("John", response.getFirstname());
		assertEquals("Smith", response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals(2, response.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> itspDTO = response.getSpecialtiesDTO().iterator();
		SpecialtyDTO spDTO = itspDTO.next();
		assertEquals("S001", spDTO.getId());
		assertEquals("Specialty 1", spDTO.getDescription());
		spDTO = itspDTO.next();
		assertEquals("S002", spDTO.getId());
		assertEquals("Specialty 2", spDTO.getDescription());

		assertEquals(doctorDTO.getId(), response.getId());
		// changes in returned object
		assertEquals(doctorDTO.getPhone(), response.getPhone());
		assertEquals(doctorDTO.getEmail(), response.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

	@Test
	public void testFindDoctorSuccess() {
		DoctorDTO doctorDTO = assertDoesNotThrow(() -> doctorService.findDoctor("D002"));

		assertEquals("D002", doctorDTO.getId());
		assertEquals("15 rue de Vaugirard", doctorDTO.getAddressDTO().getStreet1());
		assertEquals(2, ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).size());
		assertEquals("S001", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(0).getId());
		assertEquals("Specialty 1", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(0).getDescription());
		assertEquals("S002", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).getId());
		assertEquals("Specialty 2", ((List<SpecialtyDTO>) doctorDTO.getSpecialtiesDTO()).get(1).getDescription());
		assertNull(doctorDTO.getSecurityCode());
	}

	@Test
	public void testFindDoctorFailureDoctorDoesNotExist() {

		FinderException ex = assertThrows(FinderException.class, () -> doctorService.findDoctor("D003"));

		assertEquals("doctor not found", ex.getMessage());
	}

	@Test
	public void testDeleteDoctorSuccess() {

		assertTrue(doctorDAO.existsById("D002"));

		assertDoesNotThrow(() -> doctorService.deleteDoctor("D002"));

		assertFalse(doctorDAO.existsById("D002"));
	}

	@Test
	public void testDeleteDoctorFailureDoctorDoesNotExist() {

		assertFalse(doctorDAO.existsById("D003"));

		DeleteException ex = assertThrows(DeleteException.class, () -> doctorService.deleteDoctor("D003"));

		assertTrue(ex.getMessage().startsWith("doctor could not be deleted: "));
	}

	@Test
	public void testDeleteDoctorFailureDoctorIsReferringDoctor() {

		assertTrue(doctorDAO.existsById("D001"));

		DeleteException ex = assertThrows(DeleteException.class, () -> doctorService.deleteDoctor("D001"));

		assertTrue(ex.getMessage().startsWith("doctor could not be deleted: "));
		
		assertTrue(doctorDAO.existsById("D001"));
	}

}
