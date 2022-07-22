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
import fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class DoctorController {

	@Autowired
	UserService userService;

	@Autowired
	DoctorService doctorService;

	@PostMapping("/doctor")
	public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO)
			throws DuplicateKeyException, FinderException {

		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorDTO));
	}

	@PutMapping("/doctor/details")
	public ResponseEntity<DoctorDTO> updateDoctor(@Valid @RequestBody DoctorDTO doctorDTO,
			Principal principal) throws FinderException {
		
		UserDTO userDTO = userService.findUserByUsername(principal.getName());
		
		doctorDTO.setId(userDTO.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body(doctorService.updateDoctor(doctorDTO));
	}

}
