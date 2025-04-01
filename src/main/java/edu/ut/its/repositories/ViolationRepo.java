package edu.ut.its.repositories;

import edu.ut.its.models.entitys.Violation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationRepo extends MongoRepository<Violation, String> {
}
