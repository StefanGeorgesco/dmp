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
	
	@NotNull(message = "Les spécialités sont obligatoires.")
	@Size(min = 1, message = "Le médecin doit avoir au moins une spécialité.")
	@JsonProperty("specialties")
	private Collection<@Valid SpecialtyDTO> specialtiesDTO;

}
