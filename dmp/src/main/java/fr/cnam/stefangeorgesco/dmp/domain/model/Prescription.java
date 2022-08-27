package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_prescription")
@OnDelete(action = OnDeleteAction.CASCADE)
@Getter
@Setter
public class Prescription extends PatientFileItem {
	
	@Column(length = 800)
	@NotBlank(message = "La description est obligatoire.")
	private String description;

}
