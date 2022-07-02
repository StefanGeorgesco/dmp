package fr.cnam.stefangeorgesco.dmp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import fr.cnam.stefangeorgesco.dmp.authentication.domain.dto.UserDTO;
import fr.cnam.stefangeorgesco.dmp.authentication.domain.model.User;
import fr.cnam.stefangeorgesco.dmp.domain.model.Act;
import fr.cnam.stefangeorgesco.dmp.domain.model.Address;
import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;
import fr.cnam.stefangeorgesco.dmp.domain.model.Diagnosis;
import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;
import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;
import fr.cnam.stefangeorgesco.dmp.domain.model.Mail;
import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;
import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;
import fr.cnam.stefangeorgesco.dmp.domain.model.Prescription;
import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;
import fr.cnam.stefangeorgesco.dmp.domain.model.Symptom;

@Configuration
public class TestConfiguration {
	
	@Bean(name = "user")
	@Scope(value = "prototype")
	User getUser() {
		return new User();
	}
	
	@Bean(name = "userDTO")
	@Scope(value = "prototype")
	UserDTO getUserDTO() {
		return new UserDTO();
	}
	
	@Bean(name = "doctor")
	@Scope(value = "prototype")
	Doctor getDoctor() {
		return new Doctor();
	}
		
	@Bean(name = "patientFile")
	@Scope(value = "prototype")
	PatientFile getPatientFile() {
		return new PatientFile();
	}
	
	@Bean(name = "address")
	@Scope(value = "prototype")
	Address GetObjectAddressNode() {
		return new Address();
	}
	
	@Bean(name = "correspondance")
	@Scope(value = "prototype")
	Correspondance getCorrespondance() {
		return new Correspondance();
	}
	
	@Bean(name = "specialty")
	@Scope(value = "prototype")
	Specialty getSpecialty() {
		return new Specialty();
	}
			
	@Bean(name = "act")
	@Scope(value = "prototype")
	Act getAct() {
		return new Act();
	}
	
	@Bean(name = "diagnosis")
	@Scope(value = "prototype")
	Diagnosis getDiagnosis() {
		return new Diagnosis();
	}
	
	@Bean(name = "disease")
	@Scope(value = "prototype")
	Disease getDisease() {
		return new Disease();
	}
	
	@Bean(name = "mail")
	@Scope(value = "prototype")
	Mail getMail() {
		return new Mail();
	}
	
	@Bean(name = "medicalAct")
	@Scope(value = "prototype")
	MedicalAct getMedicalAct() {
		return new MedicalAct();
	}
	
	@Bean(name = "prescription")
	@Scope(value = "prototype")
	Prescription getPrescription() {
		return new Prescription();
	}
	
	@Bean(name = "symptom")
	@Scope(value = "prototype")
	Symptom getSymptom() {
		return new Symptom();
	}
	
}
