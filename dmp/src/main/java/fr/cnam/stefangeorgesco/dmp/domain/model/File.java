package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.exception.domain.CheckException;
import lombok.Data;

@Data
public abstract class File {

	@NotBlank(message = "id is mandatory")
	protected String id;

	@NotBlank(message = "firstname is mandatory")
	protected String firstname;

	@NotBlank(message = "lastname is mandatory")
	protected String lastname;

	@NotBlank(message = "phone is mandatory")
	protected String phone;

	@NotNull(message = "email is mandatory")
	@Email(message = "email must be given and respect format")
	protected String email;

	@NotNull(message = "address is mandatory")
	@Valid
	protected Address address;

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
