package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.Disease;

public interface DiseaseDAO extends CrudRepository<Disease, String> {

	@Query(nativeQuery = true, value = "select distinct * from t_disease "
			+ "where lower(t_disease.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(t_disease.description) like lower(concat('%', :keyword,'%')) limit :limit")
	public Iterable<Disease> findByIdOrDescription(@Param("keyword") String keyword, @Param("limit") int limit);

}
