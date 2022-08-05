package fr.cnam.stefangeorgesco.dmp.domain.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFileItem;

public interface PatientFileItemDAO extends CrudRepository<PatientFileItem, UUID> {
	
}
