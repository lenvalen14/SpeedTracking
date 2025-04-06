package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {
    Page<AccountDetailResponse> getAllAccounts(Pageable pageable);
    AccountDetailResponse getAccountById(String id);
    AccountDetailResponse createAccount(AccountRegisterRequest accountDTO);
    AccountDetailResponse updateAccount(String id, AccountUpdateRequest accountDTO);
    Boolean deleteAccount(String id);
}