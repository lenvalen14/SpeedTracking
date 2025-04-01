package edu.ut.its.services;

import edu.ut.its.models.dtos.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<AccountDTO> getAllAccounts();
    Optional<AccountDTO> getAccountById(String id);
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO updateAccount(String id, AccountDTO accountDTO);
    void deleteAccount(String id);
}