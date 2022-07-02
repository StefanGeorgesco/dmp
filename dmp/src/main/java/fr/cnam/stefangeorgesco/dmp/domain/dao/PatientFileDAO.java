package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

public interface PatientFileDAO extends CrudRepository<PatientFile, String> {

}
