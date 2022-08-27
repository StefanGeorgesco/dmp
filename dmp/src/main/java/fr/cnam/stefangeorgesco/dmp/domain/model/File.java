package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.password.PasswordEncoder;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_file")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class File {

	@Id
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

	@Embedded
	@NotNull(message = "L'adresse est obligatoire.")
	@Valid
	protected Address address;

	@Column(name = "security_code", nullable = false)
	protected String securityCode;
	
	public void checkUserData(User user, PasswordEncoder passwordEncoder) throws CheckException {
		
		if (user == null) {
			throw new CheckException("Impossible de vérifier un utilisateur 'null'.");
		}

		if (user.getId() == null) {
			throw new CheckException("Impossible de vérifier un utilisateur avec un identifiant 'null'.");
		}

		if (user.getSecurityCode() == null) {
			throw new CheckException("Impossible de vérifier un utilisateur avec un code de sécurité 'null'.");
		}

		if (!user.getId().equals(this.id) || !passwordEncoder.matches(user.getSecurityCode(), this.securityCode)) {
			throw new CheckException("Les données ne correspondent pas.");
		}

	}

}
