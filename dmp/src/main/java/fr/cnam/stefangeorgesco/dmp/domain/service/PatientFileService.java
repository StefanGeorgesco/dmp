package fr.cnam.stefangeorgesco.dmp.domain.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondanceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondanceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;
import fr.cnam.stefangeorgesco.dmp.utils.PasswordGenerator;

@Service
public class PatientFileService {

	@Autowired
	private RNIPPService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private DoctorDAO doctorDAO;
	
	@Autowired
	private CorrespondanceDAO correspondanceDAO;

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper patientFileModelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO) throws ApplicationException {

		rnippService.checkPatientData(patientFileDTO);

		if (patientFileDAO.existsById(patientFileDTO.getId())) {
			throw new DuplicateKeyException("patient file already exists");
		}

		patientFileDTO.setSecurityCode(PasswordGenerator.generatePassword());

		PatientFile patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setSecurityCode(bCryptPasswordEncoder.encode(patientFile.getSecurityCode()));

		try {
			patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new CreateException("patient file could not be created: " + e.getMessage());
		}

		return patientFileDTO;
	}

	public PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) throws UpdateException {

		PatientFile patientFile = patientFileDAO.findById(patientFileDTO.getId()).get();

		patientFile.setPhone(patientFileDTO.getPhone());
		patientFile.setEmail(patientFileDTO.getEmail());

		PatientFile mappedPatientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setAddress(mappedPatientFile.getAddress());

		try {
			patientFile = patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new UpdateException("patient file could not be updated: " + e.getMessage());
		}

		PatientFileDTO response = patientFileModelMapper.map(patientFile, PatientFileDTO.class);

		return response;
	}

	public PatientFileDTO findPatientFile(String id) throws FinderException {
		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(id);

		if (optionalPatientFile.isPresent()) {
			return patientFileModelMapper.map(optionalPatientFile.get(), PatientFileDTO.class);
		} else {
			throw new FinderException("patient file not found");
		}
	}

	public PatientFileDTO updateReferringDoctor(PatientFileDTO patientFileDTO) throws ApplicationException {

		Optional<PatientFile> optionalPatientFile = patientFileDAO.findById(patientFileDTO.getId());

		if (optionalPatientFile.isEmpty()) {
			throw new FinderException("patient file not found");
		}

		PatientFile patientFile = optionalPatientFile.get();

		Optional<Doctor> optionalDoctor = doctorDAO.findById(patientFileDTO.getReferringDoctorId());

		if (optionalDoctor.isEmpty()) {
			throw new FinderException("doctor not found");
		}

		Doctor doctor = optionalDoctor.get();

		patientFile.setReferringDoctor(doctor);

		try {
			patientFile = patientFileDAO.save(patientFile);
		} catch (Exception e) {
			throw new UpdateException("patient file could not be updated (referring docotor): " + e.getMessage());
		}

		PatientFileDTO response = patientFileModelMapper.map(patientFile, PatientFileDTO.class);

		return response;
	}

	public List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String string) {
		
		if ("".equals(string)) {
			return new ArrayList<PatientFileDTO>();
		}
		
		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname(string);
		
		List<PatientFileDTO> patientFilesDTO = ((List<PatientFile>) patientFiles)
				.stream()
				.map(patientFile -> patientFileModelMapper.map(patientFile, PatientFileDTO.class))
				.collect(Collectors.toList());

		return patientFilesDTO;
	}

	public CorrespondanceDTO createCorrespondance(CorrespondanceDTO correspondanceDTO) throws CreateException {
		
		Correspondance correspondance = commonModelMapper.map(correspondanceDTO, Correspondance.class);
		
		try {
			correspondance = correspondanceDAO.save(correspondance);
		} catch (Exception e) {
			throw new CreateException("correspondance could not be created: " + e.getMessage());
		}
		
		correspondanceDTO.setId(correspondance.getId());

		return correspondanceDTO;
	}

	public void deleteCorrespondance(UUID uuid) {
		
		correspondanceDAO.deleteById(uuid);
	}

}
