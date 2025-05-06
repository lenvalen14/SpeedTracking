package edu.ut.its.repositories;

import edu.ut.its.models.entities.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepo extends MongoRepository<Video, String> {

}
