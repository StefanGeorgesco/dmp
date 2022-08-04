package fr.cnam.stefangeorgesco.dmp.domain.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondence;

public interface CorrespondenceDAO extends CrudRepository<Correspondence, UUID> {

	Iterable<Correspondence> findByPatientFileId(String patientFileId);

}
