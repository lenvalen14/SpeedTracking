package edu.ut.its.repositories;

import edu.ut.its.models.entities.StreetLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StreetLogRepo extends MongoRepository<StreetLog, String> {
    List<StreetLog> findByStreet_StreetIdAndCreateAtBetween(String streetId, LocalDateTime start, LocalDateTime end);
}