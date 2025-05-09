package edu.ut.its.repositories;

import edu.ut.its.models.entities.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepo extends MongoRepository<Video, String> {
    List<Video> findByCamera_CameraId(String cameraId);
}
