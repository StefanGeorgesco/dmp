package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class PatientFileController {

	@Autowired
	UserService userService;

	@Autowired
	DoctorService doctorService;

	@Autowired
	private PatientFileService patientFileService;

	@PostMapping("/patient-file")
	public ResponseEntity<PatientFileDTO> createPatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws CheckException, DuplicateKeyException, FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		DoctorDTO doctorDTO = doctorService.findDoctor(userDTO.getId());

		patientFileDTO.setReferringDoctorDTO(doctorDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(patientFileService.createPatientFile(patientFileDTO));
	}

	@PutMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> updatePatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws FinderException {
		
		UserDTO userDTO = userService.findUserByUsername(principal.getName());
		
		patientFileDTO.setId(userDTO.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.updatePatientFile(patientFileDTO));
	}

}
