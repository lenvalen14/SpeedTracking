package edu.ut.its.repositories;

import edu.ut.its.models.entities.Street;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepo extends MongoRepository<Street, String> {
    public Boolean findByName(String name);
}
