package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.Data;

@Data
public abstract class PatientFileItem {

	private String id;

	@NotNull(message = "patient file date is mandatory")
	@PastOrPresent(message = "patient file item date must be in the past or today")
	private LocalDate date;
	
	private String comments;

}
