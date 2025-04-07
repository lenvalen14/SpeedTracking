package edu.ut.its.repositories;


import edu.ut.its.models.entities.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends MongoRepository<Vehicle, String> {
    Vehicle findByLicensePlates(String licensePlate);
}
