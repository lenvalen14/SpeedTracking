package edu.ut.its.repositories;

import edu.ut.its.models.entitys.StreetLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetLogRepo extends MongoRepository<StreetLog, String> {
}