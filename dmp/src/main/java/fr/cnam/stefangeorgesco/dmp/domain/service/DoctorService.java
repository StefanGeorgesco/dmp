package fr.cnam.stefangeorgesco.dmp.domain.service;

import java.util.Iterator;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.SpecialtyDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.utils.PasswordGenerator;

@Service
public class DoctorService {

	@Autowired
	DoctorDAO doctorDAO;

	@Autowired
	ModelMapper doctorDTOModelMapper;

	@Autowired
	SpecialtyDAO specialtyDAO;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws DuplicateKeyException {

		if (doctorDAO.existsById(doctorDTO.getId())) {
			throw new DuplicateKeyException("doctor already exists");
		}

		doctorDTO.setSecurityCode(PasswordGenerator.generatePassword());

		Iterator<SpecialtyDTO> it = doctorDTO.getSpecialtiesDTO().iterator();

		while (it.hasNext()) {
			SpecialtyDTO specialtyDTO = it.next();

			Optional<Specialty> optionalSpecialty = specialtyDAO.findById(specialtyDTO.getId());

			if (optionalSpecialty.isPresent()) {
				specialtyDTO.setDescription(optionalSpecialty.get().getDescription());
			} else {
				it.remove();
			}
		}

		Doctor doctor = doctorDTOModelMapper.map(doctorDTO, Doctor.class);

		doctor.setSecurityCode(bCryptPasswordEncoder.encode(doctorDTO.getSecurityCode()));

		doctorDAO.save(doctor);

		return doctorDTO;
	}

}
