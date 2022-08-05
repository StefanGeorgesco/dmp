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

import fr.cnam.stefangeorgesco.dmp.domain.dao.CorrespondenceDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DiseaseDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.DoctorDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.MedicalActDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileItemDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;
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
	private CorrespondenceDAO correspondenceDAO;

	@Autowired
	private DiseaseDAO diseaseDAO;

	@Autowired
	private MedicalActDAO medicalActDAO;

	@Autowired
	private PatientFileItemDAO patientFileItemDAO;

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper patientFileModelMapper;

	@Autowired
	private MapperService mapperService;

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

	public List<PatientFileDTO> findPatientFilesByIdOrFirstnameOrLastname(String q) {

		if ("".equals(q)) {
			return new ArrayList<PatientFileDTO>();
		}

		Iterable<PatientFile> patientFiles = patientFileDAO.findByIdOrFirstnameOrLastname(q);

		List<PatientFileDTO> response = ((List<PatientFile>) patientFiles).stream()
				.map(patientFile -> patientFileModelMapper.map(patientFile, PatientFileDTO.class))
				.collect(Collectors.toList());

		return response;
	}

	public CorrespondenceDTO createCorrespondence(CorrespondenceDTO correspondenceDTO) throws CreateException {

		Correspondence correspondence = commonModelMapper.map(correspondenceDTO, Correspondence.class);

		try {
			correspondence = correspondenceDAO.save(correspondence);
		} catch (Exception e) {
			throw new CreateException("correspondence could not be created: " + e.getMessage());
		}

		correspondence = correspondenceDAO.findById(correspondence.getId()).get();

		CorrespondenceDTO response = commonModelMapper.map(correspondence, CorrespondenceDTO.class);

		return response;
	}

	public void deleteCorrespondence(UUID uuid) {

		correspondenceDAO.deleteById(uuid);
	}

	public CorrespondenceDTO findCorrespondence(String id) throws FinderException {

		Optional<Correspondence> optionalCorrespondence = correspondenceDAO.findById(UUID.fromString(id));

		if (optionalCorrespondence.isEmpty()) {
			throw new FinderException("correspondence not found");
		}

		Correspondence correspondence = optionalCorrespondence.get();

		CorrespondenceDTO response = commonModelMapper.map(correspondence, CorrespondenceDTO.class);

		return response;
	}

	public List<CorrespondenceDTO> findCorrespondencesByPatientFileId(String patientFileId) {

		Iterable<Correspondence> correspondences = correspondenceDAO.findByPatientFileId(patientFileId);

		List<CorrespondenceDTO> correspondencesDTO = ((List<Correspondence>) correspondences).stream()
				.map(correspondence -> commonModelMapper.map(correspondence, CorrespondenceDTO.class))
				.collect(Collectors.toList());

		return correspondencesDTO;
	}

	public DiseaseDTO findDisease(String id) throws FinderException {

		Optional<Disease> optionalDisease = diseaseDAO.findById(id);

		if (optionalDisease.isPresent()) {
			return commonModelMapper.map(optionalDisease.get(), DiseaseDTO.class);
		} else {
			throw new FinderException("disease not found");
		}
	}

	public MedicalActDTO findMedicalAct(String id) throws FinderException {

		Optional<MedicalAct> optionalMedicalAct = medicalActDAO.findById(id);

		if (optionalMedicalAct.isPresent()) {
			return commonModelMapper.map(optionalMedicalAct.get(), MedicalActDTO.class);
		} else {
			throw new FinderException("medical act not found");
		}
	}

	public List<DiseaseDTO> findDiseasesByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<Disease> diseases = diseaseDAO.findByIdOrDescription(q, limit);

		List<DiseaseDTO> diseasesDTO = ((List<Disease>) diseases).stream()
				.map(disease -> commonModelMapper.map(disease, DiseaseDTO.class)).collect(Collectors.toList());

		return diseasesDTO;
	}

	public List<MedicalActDTO> findMedicalActsByIdOrDescription(String q, int limit) {

		if ("".equals(q)) {
			return new ArrayList<>();
		}

		Iterable<MedicalAct> medicalActs = medicalActDAO.findByIdOrDescription(q, limit);

		List<MedicalActDTO> medicalActsDTO = ((List<MedicalAct>) medicalActs).stream()
				.map(medicalAct -> commonModelMapper.map(medicalAct, MedicalActDTO.class)).collect(Collectors.toList());

		return medicalActsDTO;
	}

	public PatientFileItemDTO createPatientFileItem(PatientFileItemDTO patientFileItemDTO) throws CreateException {

		PatientFileItem patientFileItem = mapperService.mapToEntity(patientFileItemDTO);

		try {
			patientFileItem = patientFileItemDAO.save(patientFileItem);
		} catch (Exception e) {
			throw new CreateException("patient file item could not be created: " + e.getMessage());
		}
		
		patientFileItem = patientFileItemDAO.findById(patientFileItem.getId()).get();
		
		PatientFileItemDTO respsonse = mapperService.mapToDTO(patientFileItem);

		return respsonse;
	}

}
