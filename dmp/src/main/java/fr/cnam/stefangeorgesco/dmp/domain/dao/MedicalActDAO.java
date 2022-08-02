package fr.cnam.stefangeorgesco.dmp.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cnam.stefangeorgesco.dmp.domain.model.MedicalAct;

public interface MedicalActDAO extends CrudRepository<MedicalAct, String> {

	@Query(nativeQuery = true, value = "select distinct * from t_medical_act "
			+ "where lower(t_medical_act.id) like lower(concat('%', :keyword,'%')) "
			+ "or lower(t_medical_act.description) like lower(concat('%', :keyword,'%')) limit :limit")
	public Iterable<MedicalAct> findByIdOrDescription(@Param("keyword") String keyword, @Param("limit") int limit);

}
