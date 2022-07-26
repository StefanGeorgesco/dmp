package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;

public interface CorrespondanceDAO extends CrudRepository<Correspondance, Long> {

}
