package fr.cnam.stefangeorgesco.dmp.domain.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.Correspondance;

public interface CorrespondanceDAO extends CrudRepository<Correspondance, UUID> {

}
