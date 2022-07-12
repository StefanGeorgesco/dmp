package fr.cnam.stefangeorgesco.dmp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.utils.PasswordGenerator;

@Service
public class DoctorService {
	
	@Autowired
	DoctorDAO doctorDAO;
	
	@Autowired
	ModelMapper doctorDTOModelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws DuplicateKeyException {
		
		if (doctorDAO.existsById(doctorDTO.getId())) {
			throw new DuplicateKeyException("doctor already exists");
		};
		
		doctorDTO.setSecurityCode(PasswordGenerator.generatePassword());
		
		Doctor doctor = doctorDTOModelMapper.map(doctorDTO, Doctor.class);
		
		doctor.setSecurityCode(bCryptPasswordEncoder.encode(doctorDTO.getSecurityCode()));
		
		doctorDAO.save(doctor);
		
		return doctorDTO;
	}

}
