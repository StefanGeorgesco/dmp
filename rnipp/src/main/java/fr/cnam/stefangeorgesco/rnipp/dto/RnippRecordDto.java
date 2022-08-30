package fr.cnam.stefangeorgesco.rnipp.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

public class RnippRecordDto {
	
	@NotBlank(message = "L'identifiant est obligatoire.")
	private String id;

	/**
	 * Prénom.
	 */
	@NotBlank(message = "Le prénom est obligatoire.")
	private String firstname;

	/**
	 * Nom.
	 */
	@NotBlank(message = "Le nom est obligatoire.")
	private String lastname;

	/**
	 * Date de naissance.
	 */
	@NotNull(message = "La date de naissance est obligatoire.")
	@PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
	private LocalDate dateOfBirth;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
