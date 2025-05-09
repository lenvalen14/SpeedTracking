package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.AccountForgotPasswordRequest;
import edu.ut.its.models.dtos.requests.AccountLoginRequest;
import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {
    Page<AccountDetailResponse> getAllAccounts(Pageable pageable);
    AccountDetailResponse getAccountById(String id);
    AccountDetailResponse createAccount(AccountRegisterRequest accountDTO);

    String login(AccountLoginRequest accountLoginRequest);

    Account getUserDetailsFromToken(String token) throws Exception;

    AccountDetailResponse updateAccount(String id, AccountUpdateRequest accountDTO);

    AccountDetailResponse forgotPassword(String email, AccountForgotPasswordRequest request);

    Boolean deleteAccount(String id);
}