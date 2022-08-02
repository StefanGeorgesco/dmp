package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Doctor;

public interface DoctorDAO extends CrudRepository<Doctor, String> {

	@Query("select distinct doctor from Doctor doctor "
			+ "where lower(doctor.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(doctor.firstname) like lower(concat('%', :keyword,'%')) "
			+ "or lower(doctor.lastname) like lower(concat('%', :keyword,'%'))")
	public Iterable<Doctor> findByIdOrFirstnameOrLastname(@Param("keyword") String keyword);

}
