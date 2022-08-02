package fr.cnam.stefangeorgesco.dmp.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;
import fr.cnam.stefangeorgesco.dmp.utils.PasswordGenerator;

@Service
public class DoctorService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private DoctorDAO doctorDAO;

	@Autowired
	private ModelMapper commonModelMapper;
	
	@Autowired
	private ModelMapper doctorModelMapper;

	@Autowired
	private SpecialtyDAO specialtyDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws ApplicationException {

		if (doctorDAO.existsById(doctorDTO.getId())) {
			throw new DuplicateKeyException("doctor already exists");
		}

		doctorDTO.setSecurityCode(PasswordGenerator.generatePassword());

		for (SpecialtyDTO specialtyDTO: doctorDTO.getSpecialtiesDTO()) {
			Optional<Specialty> optionalSpecialty = specialtyDAO.findById(specialtyDTO.getId());

			if (optionalSpecialty.isPresent()) {
				specialtyDTO.setDescription(optionalSpecialty.get().getDescription());
			} else {
				throw new FinderException("specialty does not exist");
			}
		}

		Doctor doctor = commonModelMapper.map(doctorDTO, Doctor.class);

		doctor.setSecurityCode(bCryptPasswordEncoder.encode(doctorDTO.getSecurityCode()));

		try {
			doctorDAO.save(doctor);
		} catch (Exception e) {
			throw new CreateException("doctor could not be created:" + e.getMessage());
		}

		return doctorDTO;
	}

	public DoctorDTO findDoctor(String id) throws FinderException {
		
		Optional<Doctor> optionalDoctor = doctorDAO.findById(id);
		
		if (optionalDoctor.isPresent()) {
			return doctorModelMapper.map(optionalDoctor.get(), DoctorDTO.class);
		} else {
			throw new FinderException("doctor not found");
		}

	}

	public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws UpdateException {
		
		Doctor doctor = doctorDAO.findById(doctorDTO.getId()).get();
		
		doctor.setPhone(doctorDTO.getPhone());
		doctor.setEmail(doctorDTO.getEmail());
		
		Doctor mappedDoctor = commonModelMapper.map(doctorDTO, Doctor.class);
		
		doctor.setAddress(mappedDoctor.getAddress());
		
		try {
			doctorDAO.save(doctor);
		} catch (Exception e) {
			throw new UpdateException("doctor could not be updated: " + e.getMessage());
		}
		
		DoctorDTO response = doctorModelMapper.map(doctor, DoctorDTO.class);
		
		return response;
	}

	public void deleteDoctor(String id) throws DeleteException {
		
		try {
			doctorDAO.deleteById(id);
		} catch (Exception e) {
			throw new DeleteException("doctor could not be deleted: " + e.getMessage());
		}
		
		try {
			userService.deleteUser(id);
		} catch (DeleteException e) {
			System.out.println("no user associated to deleted doctor");
		}
		
	}

	public List<DoctorDTO> findDoctorsByIdOrFirstnameOrLastname(String string) {
		
		if ("".equals(string)) {
			return new ArrayList<DoctorDTO>();
		}
		
		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname(string);
		
		List<DoctorDTO> doctorsDTO = ((List<Doctor>) doctors)
				.stream()
				.map(doctor -> doctorModelMapper.map(doctor, DoctorDTO.class))
				.collect(Collectors.toList());

		return doctorsDTO;
	}

	public SpecialtyDTO findSpecialty(String id) throws FinderException {
		
		Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);
		
		if (optionalSpecialty.isPresent()) {
			return commonModelMapper.map(optionalSpecialty.get(), SpecialtyDTO.class);
		} else {
			throw new FinderException("specialty not found");
		}

	}

}
