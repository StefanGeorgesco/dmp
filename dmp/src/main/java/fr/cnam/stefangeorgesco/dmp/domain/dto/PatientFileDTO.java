package fr.cnam.stefangeorgesco.dmp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientFileDTO extends FileDTO {
	
	@JsonProperty("referringDoctor")
	private DoctorDTO referringDoctorDTO;
}
