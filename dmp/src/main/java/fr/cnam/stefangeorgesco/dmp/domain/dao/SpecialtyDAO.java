package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Specialty;

public interface SpecialtyDAO extends CrudRepository<Specialty, String> {

	@Query("select distinct specialty from Specialty specialty "
			+ "where lower(specialty.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(specialty.description) like lower(concat('%', :keyword,'%'))")
	public Iterable<Specialty> findByIdOrDescription(@Param("keyword") String keyword);
}
