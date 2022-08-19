package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientFileDTO extends FileDTO {
	
	@NotNull(message = "date of birth is mandatory")
	@PastOrPresent(message = "date of birth must be in the past or today")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dateOfBirth;

	private String referringDoctorId;
	
	private String referringDoctorFirstname;
	
	private String referringDoctorLastname;
	
	private List<String> referringDoctorSpecialties;
}
