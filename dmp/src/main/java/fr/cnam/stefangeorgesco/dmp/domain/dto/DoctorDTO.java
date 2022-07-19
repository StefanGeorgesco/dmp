package fr.cnam.stefangeorgesco.dmp.domain.dto;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO extends FileDTO {
	
	@NotNull(message = "specialties are mandatory")
	@Size(min = 1, message = "doctor must have at least one specialty")
	@JsonProperty("specialties")
	private Collection<@Valid SpecialtyDTO> specialtiesDTO;

}