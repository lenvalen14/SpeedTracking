package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.Camera;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraRepo extends MongoRepository<Camera, Integer> {
}
