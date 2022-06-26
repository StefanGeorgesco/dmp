package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class File {

	@Id
	@NotBlank(message = "id is mandatory")
	protected String id;

	@NotBlank(message = "firstname is mandatory")
	protected String firstname;

	@NotBlank(message = "lastname is mandatory")
	protected String lastname;

	@NotBlank(message = "phone is mandatory")
	protected String phone;

	@NotBlank(message = "email is mandatory")
	@Email(message = "email must be given and respect format")
	protected String email;

	@Embedded
	@NotNull(message = "address is mandatory")
	@Valid
	protected Address address;

	@Column(name = "security_code", nullable = false)
	protected String securityCode;

	public void checkUserData(User user) throws CheckException {

		if (user == null) {
			throw new CheckException("tried to check null user");
		}

		if (user.getId() == null) {
			throw new CheckException("tried to check user with null id");
		}

		if (user.getSecurityCode() == null) {
			throw new CheckException("tried to check user with null security code");
		}

		if (!user.getId().equals(this.id) || !user.getSecurityCode().equals(this.securityCode)) {
			throw new CheckException("data did not match");
		}

	}

}
