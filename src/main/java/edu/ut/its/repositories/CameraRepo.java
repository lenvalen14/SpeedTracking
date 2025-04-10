package edu.ut.its.repositories;

import edu.ut.its.models.entities.Camera;
import edu.ut.its.models.entities.Street;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepo extends MongoRepository<Camera, String> {
    List<Camera> findAllByStatusTrue();
    Optional<Camera> findByCameraIdAndStatusTrue(String id);
    long countByStreetAndStatusTrue(Street street);
    long countByStreet_StreetIdAndStatusTrue(String streetId);
    List<Camera> findByStreet_StreetIdAndStatusTrue(String streetId);
}
