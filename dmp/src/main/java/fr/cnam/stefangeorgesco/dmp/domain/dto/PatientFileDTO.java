package fr.cnam.stefangeorgesco.dmp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientFileDTO extends FileDTO {
	
	private DoctorDTO referringDoctorDTO;
}
