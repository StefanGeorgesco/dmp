package fr.cnam.stefangeorgesco.dmp.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
	
	@NotBlank(message = "L'identifiant est obligatoire.")
	protected String id;

	@NotBlank(message = "Le prénom est obligatoire.")
	protected String firstname;

	@NotBlank(message = "Le nom est obligatoire.")
	protected String lastname;

	@NotBlank(message = "Le numéro de téléphone est obligatoire.")
	protected String phone;

	@NotBlank(message = "L'adresse email est obligatoire.")
	@Email(message = "L'adresse email doit être fournie et respecter le format.")
	protected String email;

	@NotNull(message = "L'adresse est obligatoire.")
	@Valid
	@JsonProperty("address")
	protected AddressDTO addressDTO;

	protected String securityCode;

}
