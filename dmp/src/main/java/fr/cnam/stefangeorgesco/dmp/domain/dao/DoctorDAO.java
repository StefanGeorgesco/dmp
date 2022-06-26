package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.repository.CrudRepository;

import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

public interface DoctorDAO extends CrudRepository<Doctor, String> {

}
