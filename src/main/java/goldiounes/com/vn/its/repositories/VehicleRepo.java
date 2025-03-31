package goldiounes.com.vn.its.repositories;


import goldiounes.com.vn.its.models.entitys.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends MongoRepository<Vehicle, Integer> {
}
