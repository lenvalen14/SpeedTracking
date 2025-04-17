package edu.ut.its.services.impl;

import edu.ut.its.models.entities.Account;
import edu.ut.its.models.entities.Token;

import java.util.List;

public interface ITokenService {
    public Token addToken(Account account, String token);

    public List<Token> getAllTokensByUser(int id);

    List<Token> getAllTokensByUser(String id);

    public void deleteToken(String token);

    public Token refreshToken(String refreshToken, Account account) throws Exception;

    Number getCountToken(String id) throws Exception;

}
