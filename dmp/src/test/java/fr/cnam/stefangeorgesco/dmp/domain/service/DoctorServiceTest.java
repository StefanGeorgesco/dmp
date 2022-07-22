package fr.cnam.stefangeorgesco.dmp.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
	private DoctorDTO response;

	@Autowired
	private Specialty specialty1;

	@Autowired
	private Specialty specialty2;

	@Autowired
	private Doctor savedDoctor;

	@Autowired
	private Doctor persistentDoctor;

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

		persistentDoctor.setId(doctorDTO.getId());
		persistentDoctor.setFirstname("firstname");
		persistentDoctor.setLastname("lastname");
		persistentDoctor.setSecurityCode("securityCode");
		persistentDoctor.setSpecialties(List.of(specialty1, specialty2));
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

		savedDoctor = doctorCaptor.getValue();

		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		assertEquals(doctorDTO.getFirstname(), savedDoctor.getFirstname());
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getSpecialtiesDTO().size(), savedDoctor.getSpecialties().size());
		assertEquals(doctorDTO.getSpecialtiesDTO().iterator().next().getDescription(),
				savedDoctor.getSpecialties().iterator().next().getDescription());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertTrue(bCryptPasswordEncoder.matches(doctorDTO.getSecurityCode(), savedDoctor.getSecurityCode()));

		assertNotNull(doctorDTO.getSecurityCode());
		assertTrue(doctorDTO.getSecurityCode().length() >= 10);
		assertEquals(2, doctorDTO.getSpecialtiesDTO().size());
		Iterator<SpecialtyDTO> it = doctorDTO.getSpecialtiesDTO().iterator();
		SpecialtyDTO specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription())
				|| "S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
		specialtyDTO = it.next();
		assertTrue("S001".equals(specialtyDTO.getId()) && "First specialty".equals(specialtyDTO.getDescription())
				|| "S002".equals(specialtyDTO.getId()) && "Second specialty".equals(specialtyDTO.getDescription()));
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

	@Test
	public void testUpdateDoctorSuccess() {
		when(doctorDAO.findById(doctorDTO.getId())).thenReturn(Optional.of(persistentDoctor));
		when(doctorDAO.save(doctorCaptor.capture())).thenAnswer(invocation -> invocation.getArguments()[0]);

		response = assertDoesNotThrow(() -> doctorService.updateDoctor(doctorDTO));

		verify(doctorDAO, times(1)).findById(doctorDTO.getId());
		verify(doctorDAO, times(1)).save(any(Doctor.class));

		savedDoctor = doctorCaptor.getValue();

		// unchanged - compared to captured saved object
		assertEquals(persistentDoctor.getId(), savedDoctor.getId());
		assertEquals(persistentDoctor.getFirstname(), savedDoctor.getFirstname());
		assertEquals(persistentDoctor.getLastname(), savedDoctor.getLastname());
		assertEquals(persistentDoctor.getSecurityCode(), savedDoctor.getSecurityCode());
		assertEquals(persistentDoctor.getSpecialties().size(), savedDoctor.getSpecialties().size());
		Iterator<Specialty> itPersistentSp = persistentDoctor.getSpecialties().iterator();
		Iterator<Specialty> itSavedSp = savedDoctor.getSpecialties().iterator();
		Specialty persistentSp = itPersistentSp.next();
		Specialty savedSp = itSavedSp.next();
		assertEquals(persistentSp.getId(), savedSp.getId());
		assertEquals(persistentSp.getDescription(), savedSp.getDescription());
		persistentSp = itPersistentSp.next();
		savedSp = itSavedSp.next();
		assertEquals(persistentSp.getId(), savedSp.getId());
		assertEquals(persistentSp.getDescription(), savedSp.getDescription());
		
		// updated - compared to captured saved object
		assertEquals(doctorDTO.getId(), savedDoctor.getId());
		assertEquals(doctorDTO.getPhone(), savedDoctor.getPhone());
		assertEquals(doctorDTO.getEmail(), savedDoctor.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), savedDoctor.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), savedDoctor.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), savedDoctor.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), savedDoctor.getAddress().getCountry());

		// unchanged - compared to response DTO object (except null security code)
		assertEquals(persistentDoctor.getId(), response.getId());
		assertEquals(persistentDoctor.getFirstname(), response.getFirstname());
		assertEquals(persistentDoctor.getLastname(), response.getLastname());
		assertEquals(null, response.getSecurityCode());
		assertEquals(persistentDoctor.getSpecialties().size(), response.getSpecialtiesDTO().size());
		itPersistentSp = persistentDoctor.getSpecialties().iterator();
		Iterator<SpecialtyDTO> itSpDTO = response.getSpecialtiesDTO().iterator();
		persistentSp = itPersistentSp.next();
		SpecialtyDTO spDTO = itSpDTO.next();
		assertEquals(persistentSp.getId(), spDTO.getId());
		assertEquals(persistentSp.getDescription(), spDTO.getDescription());
		persistentSp = itPersistentSp.next();
		spDTO = itSpDTO.next();
		assertEquals(persistentSp.getId(), spDTO.getId());
		assertEquals(persistentSp.getDescription(), spDTO.getDescription());

		// updated - compared to response DTO object
		assertEquals(doctorDTO.getId(), response.getId());
		assertEquals(doctorDTO.getPhone(), response.getPhone());
		assertEquals(doctorDTO.getEmail(), response.getEmail());
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), response.getAddressDTO().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), response.getAddressDTO().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), response.getAddressDTO().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), response.getAddressDTO().getCountry());
	}

}
