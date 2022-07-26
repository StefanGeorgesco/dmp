package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.PatientFile;

public interface PatientFileDAO extends CrudRepository<PatientFile, String> {

	@Query("select distinct patientFile from PatientFile patientFile "
			+ "where lower(patientFile.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(patientFile.firstname) like lower(concat('%', :keyword,'%')) "
			+ "or lower(patientFile.lastname) like lower(concat('%', :keyword,'%'))")
	Iterable<PatientFile> findByIdOrFirstnameOrLastname(@Param("keyword") String keyword);

}
