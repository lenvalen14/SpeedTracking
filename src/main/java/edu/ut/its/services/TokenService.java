package edu.ut.its.services;

import edu.ut.its.components.JwtTokenUtils;
import edu.ut.its.models.entities.Account;
import edu.ut.its.models.entities.Token;
import edu.ut.its.repositories.TokenRepo;
import edu.ut.its.services.impl.ITokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepo tokenRepository;

    private final JwtTokenUtils jwtTokenUtil;

    @Override
    public Token addToken(Account account, String token) {
        List<Token> userTokens = tokenRepository.findByAccount_AccountId(account.getAccountId());
        int tokenCount = userTokens.size();

        if (tokenCount >= MAX_TOKENS) {

            Token tokenToDelete = userTokens.getFirst();
            tokenRepository.delete(tokenToDelete);
        }

        // Tạo token mới
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        Token newToken = Token.builder()
                .account(account)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);

        return newToken;
    }

    @Override
    public List<Token> getAllTokensByUser(int id) {
        return List.of();
    }


    @Override
    public List<Token> getAllTokensByUser(String id) {
        return tokenRepository.findByAccount_AccountId(id);
    }

    @Override
    public void deleteToken(String token) {
        Token tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity == null) {
            throw new RuntimeException("Token not found.");
        } else {
            tokenRepository.delete(tokenEntity);
        }
    }

    @Override
    public Token refreshToken(String refreshToken, Account account) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if (existingToken == null) {
            throw new Exception("Token not found");
        }
        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new Exception("Refresh token is expired");
        }
        String token = jwtTokenUtil.generateToken(account);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpirationDate(expirationDateTime);
        existingToken.setToken(token);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }


    @Override
    public Number getCountToken(String id) throws Exception {
        List<Token> tokens = tokenRepository.findByAccount_AccountId(id);
        if (tokens.isEmpty()) {
            throw new Exception("Token not found");
        }
        int countToken = 0;
        for (Token token : tokens) {
            countToken = countToken + 1;
        }
        return countToken;
    }
}
