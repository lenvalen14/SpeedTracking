package goldiounes.com.vn.its.repositories;

import goldiounes.com.vn.its.models.entitys.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends MongoRepository<Account, Integer> {
}
