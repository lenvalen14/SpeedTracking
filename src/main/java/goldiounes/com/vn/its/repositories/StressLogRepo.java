package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.StreetLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StressLogRepo extends MongoRepository<StreetLog, String> {
}
