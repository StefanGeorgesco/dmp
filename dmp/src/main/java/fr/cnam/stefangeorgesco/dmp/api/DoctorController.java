package fr.cnam.stefangeorgesco.dmp.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DuplicateKeyException;

@RestController
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@PostMapping("/doctor")
	public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO)
			throws DuplicateKeyException {

		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorDTO));
	}

}
