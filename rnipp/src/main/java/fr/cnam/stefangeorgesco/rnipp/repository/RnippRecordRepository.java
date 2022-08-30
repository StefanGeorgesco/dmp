package fr.cnam.stefangeorgesco.rnipp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.cnam.stefangeorgesco.rnipp.model.RnippRecord;

@Repository
public interface RnippRecordRepository extends CrudRepository<RnippRecord, String> {

}
