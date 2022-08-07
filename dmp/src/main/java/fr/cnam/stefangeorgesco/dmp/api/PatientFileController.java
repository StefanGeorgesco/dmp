package fr.cnam.stefangeorgesco.dmp.api;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import fr.cnam.stefangeorgesco.dmp.domain.dto.CorrespondenceDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DiseaseDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.DoctorDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.MedicalActDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileDTO;
import fr.cnam.stefangeorgesco.dmp.domain.dto.PatientFileItemDTO;
import fr.cnam.stefangeorgesco.dmp.domain.service.PatientFileService;
import fr.cnam.stefangeorgesco.dmp.exception.domain.ApplicationException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CreateException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.FinderException;
import fr.cnam.stefangeorgesco.dmp.exception.domain.UpdateException;

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

		return ResponseEntity.ok(patientFileService.updatePatientFile(patientFileDTO));
	}

	@GetMapping("/patient-file/details")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(Principal principal) throws FinderException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		return ResponseEntity.ok(patientFileService.findPatientFile(userDTO.getId()));
	}

	@GetMapping("/patient-file/details/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findPatientCorrespondences(Principal principal)
			throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		return ResponseEntity.ok(patientFileService.findCorrespondencesByPatientFileId(userId));
	}

	@GetMapping("/patient-file/details/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientPatientFileItems(Principal principal)
			throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		return ResponseEntity.ok(patientFileService.findPatientFileItemsByPatientFileId(userId));
	}

	@GetMapping("/patient-file/{id}")
	public ResponseEntity<PatientFileDTO> getPatientFileDetails(@PathVariable String id, Principal principal)
			throws ApplicationException {

		return ResponseEntity.ok(patientFileService.findPatientFile(id));
	}

	@PutMapping("/patient-file/{id}/referring-doctor")
	public ResponseEntity<PatientFileDTO> updateReferringDoctor(@PathVariable String id,
			@Valid @RequestBody DoctorDTO doctorDTO) throws ApplicationException {

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		patientFileDTO.setReferringDoctorId(doctorDTO.getId());

		return ResponseEntity.ok(patientFileService.updateReferringDoctor(patientFileDTO));
	}

	@GetMapping("/patient-file")
	public ResponseEntity<List<PatientFileDTO>> findPatientFilesByIdOrFirstnameOrLastname(@RequestParam String q)
			throws FinderException {

		return ResponseEntity.status(HttpStatus.OK)
				.body(patientFileService.findPatientFilesByIdOrFirstnameOrLastname(q));
	}

	@PostMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<CorrespondenceDTO> createCorrespondence(
			@Valid @RequestBody CorrespondenceDTO correspondenceDTO, @PathVariable String id, Principal principal)
			throws ApplicationException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(id);

		if (!userDTO.getId().equals(patientFileDTO.getReferringDoctorId())) {
			throw new CreateException("user is not referring doctor");
		}

		if (userDTO.getId().equals(correspondenceDTO.getDoctorId())) {
			throw new CreateException("trying to create correspondence for referring doctor");
		}

		correspondenceDTO.setPatientFileId(patientFileDTO.getId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(patientFileService.createCorrespondence(correspondenceDTO));

	}

	@PostMapping("/patient-file/{id}/item")
	public ResponseEntity<PatientFileItemDTO> createPatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String id, Principal principal)
			throws Exception {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("user is not referring nor corresponding doctor");
		}

		patientFileItemDTO.setAuthoringDoctorId(userId);
		patientFileItemDTO.setPatientFileId(id);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(patientFileService.createPatientFileItem(patientFileItemDTO));
	}
	
	@GetMapping("/patient-file/{id}/item")
	public ResponseEntity<List<PatientFileItemDTO>> findPatientFileItemsByPatientFileId(@PathVariable String id, Principal principal) throws FinderException {
		
		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("user is not referring nor corresponding doctor");
		}
		
		return ResponseEntity.ok(patientFileService.findPatientFileItemsByPatientFileId(id));
	}

	@PutMapping("/patient-file/{patienfFileId}/item/{itemId}")
	public ResponseEntity<PatientFileItemDTO> updatePatientFileItem(
			@Valid @RequestBody PatientFileItemDTO patientFileItemDTO, @PathVariable String patienfFileId,
			@PathVariable String itemId, Principal principal) throws ApplicationException {
		
		String userId = userService.findUserByUsername(principal.getName()).getId();
		
		UUID patientFileItemId = UUID.fromString(itemId);
		
		patientFileItemDTO.setId(patientFileItemId);

		PatientFileItemDTO storedPatientFileItemDTO = patientFileService.findPatientFileItem(patientFileItemId);
		
		if (!patienfFileId.equals(storedPatientFileItemDTO.getPatientFileId())) {
			throw new FinderException("patient file item not found for patient file '" + patienfFileId + "'"); 
		}
		
		boolean userIsAuthor = userId.equals(storedPatientFileItemDTO.getAuthoringDoctorId());
		
		if (!userIsAuthor) {
			throw new UpdateException("user is not the author of patient file item and can not modify it");
		}
		
		String referringDoctorId = patientFileService.findPatientFile(patienfFileId).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(patienfFileId);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("user is not referring nor corresponding doctor");
		}

		return ResponseEntity.ok(patientFileService.updatePatientFileItem(patientFileItemDTO));
	}

	@DeleteMapping("/patient-file/{patientFileId}/correspondence/{correspondenceId}")
	public ResponseEntity<RestResponse> deleteCorrespondence(@PathVariable String patientFileId,
			@PathVariable String correspondenceId, Principal principal) throws ApplicationException {

		UserDTO userDTO = userService.findUserByUsername(principal.getName());

		PatientFileDTO patientFileDTO = patientFileService.findPatientFile(patientFileId);

		if (!userDTO.getId().equals(patientFileDTO.getReferringDoctorId())) {
			throw new CreateException("user is not referring doctor");
		}

		CorrespondenceDTO storedCorrespondenceDTO = patientFileService.findCorrespondence(correspondenceId);

		if (!patientFileId.equals(storedCorrespondenceDTO.getPatientFileId())) {
			throw new FinderException("correspondence not found for patient file '" + patientFileId + "'");
		}

		patientFileService.deleteCorrespondence(UUID.fromString(correspondenceId));

		RestResponse response = new RestResponse(HttpStatus.OK.value(), "correspondence was deleted");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/patient-file/{id}/correspondence")
	public ResponseEntity<List<CorrespondenceDTO>> findCorrespondencesByPatientFileId(@PathVariable String id,
			Principal principal) throws FinderException {

		String userId = userService.findUserByUsername(principal.getName()).getId();

		String referringDoctorId = patientFileService.findPatientFile(id).getReferringDoctorId();

		List<CorrespondenceDTO> correspondencesDTO = patientFileService.findCorrespondencesByPatientFileId(id);

		LocalDate now = LocalDate.now();

		boolean userIsReferringDoctor = userId.equals(referringDoctorId);

		boolean userIsCorrespondingDoctor = correspondencesDTO.stream()
				.filter(correspondence -> correspondence.getDateUntil().compareTo(now) >= 0)
				.map(CorrespondenceDTO::getDoctorId).collect(Collectors.toList()).contains(userId);

		if (!userIsReferringDoctor && !userIsCorrespondingDoctor) {
			throw new FinderException("user is not referring nor corresponding doctor");
		}

		return ResponseEntity.ok(correspondencesDTO);

	}

	@GetMapping("/disease/{id}")
	public ResponseEntity<DiseaseDTO> getDisease(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(patientFileService.findDisease(id));
	}

	@GetMapping("/disease")
	public ResponseEntity<List<DiseaseDTO>> getDiseases(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) throws FinderException {

		return ResponseEntity.ok(patientFileService.findDiseasesByIdOrDescription(q, limit));
	}

	@GetMapping("/medical-act/{id}")
	public ResponseEntity<MedicalActDTO> getMedicalAct(@PathVariable String id) throws FinderException {

		return ResponseEntity.ok(patientFileService.findMedicalAct(id));
	}

	@GetMapping("/medical-act")
	public ResponseEntity<List<MedicalActDTO>> getMedicalActs(@RequestParam String q,
			@RequestParam(required = false, defaultValue = "30") int limit) throws FinderException {

		return ResponseEntity.ok(patientFileService.findMedicalActsByIdOrDescription(q, limit));
	}

}
