package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.service.UserService;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class PatientFileController {

	@Autowired
	private UserService userService;

	@Autowired
	private PatientFileService patientFileService;

	@PostMapping("/patient-file")
	public ResponseEntity<PatientFileDTO> createPatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws ApplicationException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		patientFileDTO.setReferringDoctorId(userDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(patientFileService.createPatientFile(patientFileDTO));
	}

	@PutMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> updatePatientFile(@Valid @RequestBody PatientFileDTO patientFileDTO,
			Principal principal) throws ApplicationException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		patientFileDTO.setId(userDTO.getId());

		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.updatePatientFile(patientFileDTO));
	}

	@GetMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(Principal principal) throws FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.findPatientFile(userDTO.getId()));
	}

	@GetMapping("/patient-file/{id}")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(@PathVariable String id, Principal principal)
			throws ApplicationException {

		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.findPatientFile(id));
	}

	@PutMapping("/patient-file/{id}/referring-doctor")
	public ResponseEntity<PatientFileDTO> updateReferringDoctor(@PathVariable String id,
			@Valid @RequestBody DoctorDTO doctorDTO) throws ApplicationException {

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		patientFileDTO.setReferringDoctorId(doctorDTO.getId());

		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.updateReferringDoctor(patientFileDTO));
	}

	@GetMapping("/patient-file")
	 public ResponseEntity<List<PatientFileDTO>> findPatientFilesByIdOrFirstnameOrLastname(@RequestParam String q) throws FinderException {
		
		return ResponseEntity.status(HttpStatus.OK).body(patientFileService.findPatientFilesByIdOrFirstnameOrLastname(q));
	 }

}
