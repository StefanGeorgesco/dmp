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
import fr.cnam.stefangeorgesco.dmp.domain.dao.FileDAO;
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

/**
 * Classe de service pour la gestion des dossiers de médecins et des spécialités médicales.
 * 
 * @author Stéfan Georgesco
 *
 */
@Service
public class DoctorService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileDAO fileDAO;

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

	/**
	 * Service de création d'un dossier de médecin. Le service qu'un dossier 
	 * @param doctorDTO l'objet {@link fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO}
	 * représentant le dossier de médecin à créer.
	 * @return
	 * @throws ApplicationException
	 */
	public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws ApplicationException {

		if (fileDAO.existsById(doctorDTO.getId())) {
			throw new DuplicateKeyException("Un dossier avec cet identifiant existe déjà.");
		}

		doctorDTO.setSecurityCode(PasswordGenerator.generatePassword());

		for (SpecialtyDTO specialtyDTO : doctorDTO.getSpecialtiesDTO()) {
			Optional<Specialty> optionalSpecialty = specialtyDAO.findById(specialtyDTO.getId());

			if (optionalSpecialty.isPresent()) {
				specialtyDTO.setDescription(optionalSpecialty.get().getDescription());
			} else {
				throw new FinderException("La spécialité n'existe pas.");
			}
		}

		Doctor doctor = commonModelMapper.map(doctorDTO, Doctor.class);

		doctor.setSecurityCode(bCryptPasswordEncoder.encode(doctorDTO.getSecurityCode()));

		try {
			doctorDAO.save(doctor);
		} catch (Exception e) {
			throw new CreateException("Le dossier de médecin n'a pas pu être créé.");
		}

		return doctorDTO;
	}

	public DoctorDTO findDoctor(String id) throws FinderException {

		Optional<Doctor> optionalDoctor = doctorDAO.findById(id);

		if (optionalDoctor.isPresent()) {
			return doctorModelMapper.map(optionalDoctor.get(), DoctorDTO.class);
		} else {
			throw new FinderException("Le dossier de médecin n'a pas été trouvé.");
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
			throw new UpdateException("Le dossier de médecin n'a pas pu être modifié.");
		}

		DoctorDTO response = doctorModelMapper.map(doctor, DoctorDTO.class);

		return response;
	}

	public void deleteDoctor(String id) throws DeleteException {

		try {
			doctorDAO.deleteById(id);
		} catch (Exception e) {
			throw new DeleteException("Le dossier de médecin n'a pas pu être supprimé.");
		}

		try {
			userService.deleteUser(id);
		} catch (DeleteException e) {
			System.out.println("Pas de compte utilisateur associé au dossier de médecin supprimé.");
		}

	}

	public List<DoctorDTO> findDoctorsByIdOrFirstnameOrLastname(String string) {

		if ("".equals(string)) {
			return new ArrayList<DoctorDTO>();
		}

		Iterable<Doctor> doctors = doctorDAO.findByIdOrFirstnameOrLastname(string);

		List<DoctorDTO> doctorsDTO = ((List<Doctor>) doctors).stream()
				.map(doctor -> doctorModelMapper.map(doctor, DoctorDTO.class)).collect(Collectors.toList());

		return doctorsDTO;
	}

	public SpecialtyDTO findSpecialty(String id) throws FinderException {

		Optional<Specialty> optionalSpecialty = specialtyDAO.findById(id);

		if (optionalSpecialty.isPresent()) {
			return commonModelMapper.map(optionalSpecialty.get(), SpecialtyDTO.class);
		} else {
			throw new FinderException("Spécialité non trouvée.");
		}

	}

	public List<SpecialtyDTO> findSpecialtiesByIdOrDescription(String string) {
		
		if ("".equals(string)) {
			return new ArrayList<>();
		}
		
		Iterable<Specialty> specialties = specialtyDAO.findByIdOrDescription(string);

		List<SpecialtyDTO> specialtiesDTO = ((List<Specialty>) specialties).stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());

		return specialtiesDTO;
	}

	public List<SpecialtyDTO> findAllSpecialties() {
		Iterable<Specialty> specialties = specialtyDAO.findAll();

		List<SpecialtyDTO> specialtiesDTO = ((List<Specialty>) specialties).stream()
				.map(specialty -> commonModelMapper.map(specialty, SpecialtyDTO.class)).collect(Collectors.toList());

		return specialtiesDTO;
	}

}
