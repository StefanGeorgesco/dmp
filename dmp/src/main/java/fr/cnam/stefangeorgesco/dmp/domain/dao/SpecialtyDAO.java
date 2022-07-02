package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

public interface SpecialtyDAO extends CrudRepository<Specialty, String> {

}
