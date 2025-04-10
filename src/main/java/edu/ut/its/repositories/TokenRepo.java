package edu.ut.its.repositories;


import edu.ut.its.models.entities.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepo extends MongoRepository<Token, String> {

    List<Token> findByAccount_AccountId(String accountId);

    Token findByToken(String token);

    Token findByRefreshToken(String refreshToken);
}
