package fr.cnam.stefangeorgesco.dmp.domain.model;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Correspondance {
	
	private String id;
	
	@NotNull(message = "correspondance date is mandatory")
	@Future(message = "correpondance date must be in the future")
	private LocalDate dateUntil;

}
