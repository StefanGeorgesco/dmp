package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant un courrier adressé par le médecin référent à un autre
 * médecin.
 * 
 * @author Stéfan Georgesco
 *
 */
@Entity
@Table(name = "t_mail")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Mail extends PatientFileItem {

	/**
	 * Texte du courrier.
	 */
	@Column(length = 1000)
	@NotBlank(message = "Le texte du courrier est obligatoire.")
	private String text;

	/**
	 * Médecin destinataire.
	 */
	@ManyToOne
	@JoinColumn(name = "recipient_doctor_id")
	@NotNull(message = "Le médecin destinataire est obligatoire.")
	private Doctor recipientDoctor;

}
