package fr.cnam.stefangeorgesco.dmp.configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondanceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ModelMapperTest {
	
	@Autowired
	private Address address;
	
	@Autowired
	private Specialty specialty1;
	
	@Autowired
	private Specialty specialty2;
	
	@Autowired
	private Doctor doctor;
	
	@Autowired
	private PatientFile patientFile;
	
	@Autowired
	private Correspondance correspondance;
	
	@Autowired
	private AddressDTO addressDTO;
	
	@Autowired
	private SpecialtyDTO specialtyDTO1;
	
	@Autowired
	private SpecialtyDTO specialtyDTO2;
	
	@Autowired
	private DoctorDTO doctorDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;
	
	@Autowired
	private CorrespondanceDTO correspondanceDTO;
	
	@Autowired
	private ModelMapper commonModelMapper;
	
	@Autowired
	private ModelMapper doctorModelMapper;

	@Autowired
	private ModelMapper patientFileModelMapper;
	
	private List<Specialty> specialties = new ArrayList<>();
	
	private List<SpecialtyDTO> specialtiesDTO = new ArrayList<>();
	
	@Test
	public void testModelMapperDoctorDTO2Doctor() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		specialtyDTO1.setId("S001");
		specialtyDTO1.setDescription("First specialty");
		specialtyDTO2.setId("S002");
		specialtyDTO2.setDescription("Second specialty");
		specialtiesDTO.add(specialtyDTO1);
		specialtiesDTO.add(specialtyDTO2);
		doctorDTO.setId("P001");
		doctorDTO.setFirstname("Patrick");
		doctorDTO.setLastname("Dubois");
		doctorDTO.setPhone("9876543210");
		doctorDTO.setEmail("patrick.dubois@mail.fr");
		doctorDTO.setAddressDTO(addressDTO);
		doctorDTO.setSpecialtiesDTO(specialtiesDTO);
		doctorDTO.setSecurityCode("code");
		
		doctor = commonModelMapper.map(doctorDTO, Doctor.class);
		
		assertEquals(doctorDTO.getAddressDTO().getStreet1(), doctor.getAddress().getStreet1());
		assertEquals(doctorDTO.getAddressDTO().getZipcode(), doctor.getAddress().getZipcode());
		assertEquals(doctorDTO.getAddressDTO().getCity(), doctor.getAddress().getCity());
		assertEquals(doctorDTO.getAddressDTO().getCountry(), doctor.getAddress().getCountry());
		assertEquals(doctorDTO.getId(), doctor.getId());
		assertEquals(doctorDTO.getFirstname(), doctor.getFirstname());
		assertEquals(doctorDTO.getLastname(), doctor.getLastname());
		assertEquals(doctorDTO.getEmail(), doctor.getEmail());
		assertEquals(doctorDTO.getSpecialtiesDTO().size(), doctor.getSpecialties().size());
		Iterator<SpecialtyDTO> itDTO = doctorDTO.getSpecialtiesDTO().iterator();
		Iterator<Specialty> it = doctor.getSpecialties().iterator();
		SpecialtyDTO spDTO = itDTO.next();
		Specialty sp = it.next();
		assertEquals(spDTO.getId(), sp.getId());
		assertEquals(spDTO.getDescription(), sp.getDescription());
		spDTO = itDTO.next();
		sp = it.next();
		assertEquals(spDTO.getId(), sp.getId());
		assertEquals(spDTO.getDescription(), sp.getDescription());
		assertEquals(doctorDTO.getSecurityCode(), doctor.getSecurityCode());
	}
	
	@Test
	public void testModelMapperDoctor2DoctorDTO() {
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		specialty1.setId("S001");
		specialty1.setDescription("First specialty");
		specialty2.setId("S002");
		specialty2.setDescription("Second specialty");
		specialties.add(specialty1);
		specialties.add(specialty2);
		doctor.setId("P001");
		doctor.setFirstname("Patrick");
		doctor.setLastname("Dubois");
		doctor.setPhone("9876543210");
		doctor.setEmail("patrick.dubois@mail.fr");
		doctor.setAddress(address);
		doctor.setSpecialties(specialties);
		doctorDTO.setSecurityCode("code");
		
		doctorDTO = doctorModelMapper.map(doctor, DoctorDTO.class);
		
		assertEquals(doctor.getAddress().getStreet1(), doctorDTO.getAddressDTO().getStreet1());
		assertEquals(doctor.getAddress().getZipcode(), doctorDTO.getAddressDTO().getZipcode());
		assertEquals(doctor.getAddress().getCity(), doctorDTO.getAddressDTO().getCity());
		assertEquals(doctor.getAddress().getCountry(), doctorDTO.getAddressDTO().getCountry());
		assertEquals(doctor.getId(), doctorDTO.getId());
		assertEquals(doctor.getFirstname(), doctorDTO.getFirstname());
		assertEquals(doctor.getLastname(), doctorDTO.getLastname());
		assertEquals(doctor.getEmail(), doctorDTO.getEmail());
		assertEquals(doctor.getSpecialties().size(), doctorDTO.getSpecialtiesDTO().size());
		Iterator<Specialty> it = doctor.getSpecialties().iterator();
		Iterator<SpecialtyDTO> itDTO = doctorDTO.getSpecialtiesDTO().iterator();
		Specialty sp = it.next();
		SpecialtyDTO spDTO = itDTO.next();
		assertEquals(sp.getId(), spDTO.getId());
		assertEquals(sp.getDescription(), spDTO.getDescription());
		sp = it.next();
		spDTO = itDTO.next();
		assertEquals(sp.getId(), spDTO.getId());
		assertEquals(sp.getDescription(), spDTO.getDescription());
		assertEquals(null, doctorDTO.getSecurityCode());
	}
	
	@Test
	public void testModelMapperPatientFileDTO2PatientFile() {
		addressDTO.setStreet1("1 Rue Lecourbe");
		addressDTO.setZipcode("75015");
		addressDTO.setCity("Paris");
		addressDTO.setCountry("France");
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(addressDTO);
		patientFileDTO.setReferringDoctorId("D001");
		patientFileDTO.setSecurityCode("code");
		
		patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);
		
		assertEquals(patientFileDTO.getId(), patientFile.getId());
		assertEquals(patientFileDTO.getFirstname(), patientFile.getFirstname());
		assertEquals(patientFileDTO.getLastname(), patientFile.getLastname());
		assertEquals(patientFileDTO.getPhone(), patientFile.getPhone());
		assertEquals(patientFileDTO.getEmail(), patientFile.getEmail());
		assertEquals(patientFileDTO.getAddressDTO().getStreet1(), patientFile.getAddress().getStreet1());
		assertEquals(patientFileDTO.getAddressDTO().getZipcode(), patientFile.getAddress().getZipcode());
		assertEquals(patientFileDTO.getAddressDTO().getCity(), patientFile.getAddress().getCity());
		assertEquals(patientFileDTO.getAddressDTO().getCountry(), patientFile.getAddress().getCountry());
		assertEquals(patientFileDTO.getReferringDoctorId(), patientFile.getReferringDoctor().getId());
		assertEquals(patientFileDTO.getSecurityCode(), patientFile.getSecurityCode());
	}
	
	@Test
	public void testModelMapperPatientFile2PatientFileDTO() {
		address.setStreet1("1 Rue Lecourbe");
		address.setZipcode("75015");
		address.setCity("Paris");
		address.setCountry("France");
		doctor.setId("D001");
		patientFile.setId("P001");
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(address);
		patientFile.setSecurityCode("code");
		patientFile.setReferringDoctor(doctor);
		
		patientFileDTO = patientFileModelMapper.map(patientFile, PatientFileDTO.class);
		
		assertEquals(patientFile.getId(), patientFileDTO.getId());
		assertEquals(patientFile.getFirstname(), patientFileDTO.getFirstname());
		assertEquals(patientFile.getLastname(), patientFileDTO.getLastname());
		assertEquals(patientFile.getPhone(), patientFileDTO.getPhone());
		assertEquals(patientFile.getEmail(), patientFileDTO.getEmail());
		assertEquals(patientFile.getAddress().getStreet1(), patientFileDTO.getAddressDTO().getStreet1());
		assertEquals(patientFile.getAddress().getZipcode(), patientFileDTO.getAddressDTO().getZipcode());
		assertEquals(patientFile.getAddress().getCity(), patientFileDTO.getAddressDTO().getCity());
		assertEquals(patientFile.getAddress().getCountry(), patientFileDTO.getAddressDTO().getCountry());
		assertEquals(patientFile.getReferringDoctor().getId(), patientFileDTO.getReferringDoctorId());
		assertEquals(null, patientFileDTO.getSecurityCode());
	}
	
	@Test
	public void testModelMapperCorrespondanceDTO2Correspondance() {
		correspondanceDTO.setId(1L);
		correspondanceDTO.setDateUntil(LocalDate.of(2022, 7, 21));
		correspondanceDTO.setDoctorId("D001");
		correspondanceDTO.setPatientFileId("P001");
		
		correspondance = commonModelMapper.map(correspondanceDTO, Correspondance.class);
		
		assertEquals(correspondanceDTO.getId(), correspondance.getId());
		assertEquals(correspondanceDTO.getDateUntil(), correspondance.getDateUntil());
		assertEquals(correspondanceDTO.getDoctorId(), correspondance.getDoctor().getId());
		assertEquals(correspondanceDTO.getPatientFileId(), correspondance.getPatientFile().getId());
	}
	
	@Test
	public void testModelMapperCorrespondance2CorrespondanceDTO() {
		doctor.setId("D001");
		patientFile.setId("P001");
		correspondance.setId(1L);
		correspondance.setDateUntil(LocalDate.of(2022, 7, 21));
		correspondance.setDoctor(doctor);
		correspondance.setPatientFile(patientFile);
		
		correspondanceDTO = commonModelMapper.map(correspondance, CorrespondanceDTO.class);
		
		assertEquals(correspondance.getId(), correspondanceDTO.getId());
		assertEquals(correspondance.getDateUntil(), correspondanceDTO.getDateUntil());
		assertEquals(correspondance.getDoctor().getId(), correspondanceDTO.getDoctorId());
		assertEquals(correspondance.getPatientFile().getId(), correspondanceDTO.getPatientFileId());
	}
}
