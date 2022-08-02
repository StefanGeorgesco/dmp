package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import fr.cnam.stefangeorgesco.dmp.domain.dto.SpecialtyDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.DoctorService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.DeleteException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;

@RestController
public class DoctorController {

	@Autowired
	UserService userService;

	@Autowired
	DoctorService doctorService;

	@PostMapping("/doctor")
	public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) throws ApplicationException {

		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorDTO));
	}

	@PutMapping("/doctor/details")
	public ResponseEntity<DoctorDTO> updateDoctor(@Valid @RequestBody DoctorDTO doctorDTO, Principal principal)
			throws ApplicationException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		doctorDTO.setId(userDTO.getId());

		return ResponseEntity.ok(doctorService.updateDoctor(doctorDTO));
	}

	@GetMapping("/doctor/details")
	public ResponseEntity<DoctorDTO> getDoctorDetails(Principal principal) throws FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		return ResponseEntity.ok(doctorService.findDoctor(userDTO.getId()));
	}

	@GetMapping("/doctor/{id}")
	public ResponseEntity<DoctorDTO> getDoctorDetails(@PathVariable String id, Principal principal)
			throws FinderException {

		return ResponseEntity.ok(doctorService.findDoctor(id));
	}

	@DeleteMapping("/doctor/{id}")
	public ResponseEntity<RestResponse> deleteDoctor(@PathVariable String id) throws DeleteException {

		doctorService.deleteDoctor(id);

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "doctor was deleted");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/doctor")
	 public ResponseEntity<List<DoctorDTO>> findDoctorsByIdOrFirstnameOrLastname(@RequestParam String q) throws FinderException {
		
		return ResponseEntity.ok(doctorService.findDoctorsByIdOrFirstnameOrLastname(q));
	 }
	
	@GetMapping("/specialty/{id}")
	public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable String id) throws FinderException {
		
		return ResponseEntity.ok(doctorService.findSpecialty(id));
	}

	@GetMapping("/specialty")
	public ResponseEntity<List<SpecialtyDTO>> getSpecialties(@RequestParam String q) throws FinderException {
		
		return ResponseEntity.ok(doctorService.findSpecialtiesByIdOrDescription(q));
	}

}
