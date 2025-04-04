package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.responses.AccountDetailResponse;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<AccountDetailResponse> getAllAccounts();
    AccountDetailResponse getAccountById(String id);
    AccountDetailResponse createAccount(AccountDetailResponse accountDTO);
    AccountDetailResponse updateAccount(String id, AccountDetailResponse accountDTO);
    void deleteAccount(String id);
}