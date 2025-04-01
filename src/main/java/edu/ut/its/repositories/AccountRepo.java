package edu.ut.its.repositories;

import edu.ut.its.models.entitys.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepo extends MongoRepository<Account, String> {
    boolean existsByEmail(String email);
    Optional<Account> findByAccountIdAndStatusTrue(String id);
    List<Account> findAllByStatusTrue();
}
