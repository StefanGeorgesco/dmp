package fr.cnam.stefangeorgesco.rnipp.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.cnam.stefangeorgesco.rnipp.dto.RnippRecordDto;
import fr.cnam.stefangeorgesco.rnipp.service.RnippService;

@RestController
public class RnippController {
	
	@Autowired
	RnippService rnippService;
	
	@PostMapping("/check")
	public ResponseEntity<RestResponse> checkData(@Valid @RequestBody RnippRecordDto rnippRecordDto) {
		
		boolean result = rnippService.checkData(rnippRecordDto);
		
		String message = result ? "vérification positive." : "vérification négative.";
		
		RestResponse response = new RestResponse(result , message);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
