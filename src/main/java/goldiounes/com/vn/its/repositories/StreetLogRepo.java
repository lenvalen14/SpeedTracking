package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.StreetLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetLogRepo extends MongoRepository<StreetLog, String> {
}