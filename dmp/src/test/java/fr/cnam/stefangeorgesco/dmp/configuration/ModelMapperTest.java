package fr.cnam.stefangeorgesco.dmp.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import fr.cnam.stefangeorgesco.dmp.domain.dto.AddressDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ModelMapperTest {
	
	@Autowired
	private Doctor doctor;
	
	@Autowired
	private Address patientAddress;
	
	@Autowired
	private PatientFile patientFile;
	
	@Autowired
	private AddressDTO patientFileAddressDTO;

	@Autowired
	private PatientFileDTO patientFileDTO;

	@Autowired
	private ModelMapper patientFileModelMapper;
	
	@Autowired
	private ModelMapper patientFileDTOModelMapper;

	@Test
	public void testPatientFileDTOModelMapper() {
		patientFileAddressDTO.setStreet1("1 Rue Lecourbe");
		patientFileAddressDTO.setZipcode("75015");
		patientFileAddressDTO.setCity("Paris");
		patientFileAddressDTO.setCountry("France");
		patientFileDTO.setId("P001");
		patientFileDTO.setFirstname("Patrick");
		patientFileDTO.setLastname("Dubois");
		patientFileDTO.setPhone("9876543210");
		patientFileDTO.setEmail("patrick.dubois@mail.fr");
		patientFileDTO.setAddressDTO(patientFileAddressDTO);
		patientFileDTO.setReferringDoctorId("D001");
		
		patientFile = patientFileDTOModelMapper.map(patientFileDTO, PatientFile.class);
		
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
	}
	
	@Test
	public void testPatientFileModelMapper() {
		patientAddress.setStreet1("1 Rue Lecourbe");
		patientAddress.setZipcode("75015");
		patientAddress.setCity("Paris");
		patientAddress.setCountry("France");
		doctor.setId("D001");
		patientFile.setId("P001");
		patientFile.setFirstname("Patrick");
		patientFile.setLastname("Dubois");
		patientFile.setPhone("9876543210");
		patientFile.setEmail("patrick.dubois@mail.fr");
		patientFile.setAddress(patientAddress);
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
	}
}
