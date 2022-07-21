package fr.cnam.stefangeorgesco.dmp.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.cnam.stefangeorgesco.dmp.domain.dao.PatientFileDAO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.utils.PasswordGenerator;

@Service
public class PatientFileService {

	@Autowired
	private RNIPPService rnippService;

	@Autowired
	private PatientFileDAO patientFileDAO;

	@Autowired
	private ModelMapper commonModelMapper;

	@Autowired
	private ModelMapper patientFileModelMapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public PatientFileDTO createPatientFile(PatientFileDTO patientFileDTO)
			throws DuplicateKeyException, CheckException {

		rnippService.checkPatientData(patientFileDTO);

		if (patientFileDAO.existsById(patientFileDTO.getId())) {
			throw new DuplicateKeyException("patient file already exists");
		}

		patientFileDTO.setSecurityCode(PasswordGenerator.generatePassword());

		PatientFile patientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setSecurityCode(bCryptPasswordEncoder.encode(patientFile.getSecurityCode()));

		patientFileDAO.save(patientFile);

		return patientFileDTO;
	}

	public PatientFileDTO updatePatientFile(PatientFileDTO patientFileDTO) {

		PatientFile patientFile = patientFileDAO.findById(patientFileDTO.getId()).get();

		patientFile.setPhone(patientFileDTO.getPhone());
		patientFile.setEmail(patientFileDTO.getEmail());

		PatientFile mappedPatientFile = commonModelMapper.map(patientFileDTO, PatientFile.class);

		patientFile.setAddress(mappedPatientFile.getAddress());

		patientFile = patientFileDAO.save(patientFile);

		PatientFileDTO response = patientFileModelMapper.map(patientFile, PatientFileDTO.class);

		return response;
	}

}
