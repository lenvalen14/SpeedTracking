package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.AccountDTO;
import goldiounes.com.vn.its.models.entitys.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<AccountDTO> getAllAccounts();
    Optional<AccountDTO> getAccountById(String id);
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO updateAccount(String id, AccountDTO accountDTO);
    void deleteAccount(String id);
}