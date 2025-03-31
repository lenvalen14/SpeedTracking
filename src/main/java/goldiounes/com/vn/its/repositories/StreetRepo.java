package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.Street;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepo extends MongoRepository<Street, String> {
}
