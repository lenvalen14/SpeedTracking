package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.AccountMapper;
import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.enums.AccountRole;
import edu.ut.its.models.entities.Account;
import edu.ut.its.repositories.AccountRepo;
import edu.ut.its.services.impl.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<AccountDetailResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepo.findAll(pageable);

        if (accounts.isEmpty()) {
            throw new DataNotFoundException("No Account Found!");
        }

        return accounts.map(accountMapper::toAccountDTO);
    }

    @Override
    public AccountDetailResponse getAccountById(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toAccountDTO(account);
    }

    @Override
    public AccountDetailResponse createAccount(AccountRegisterRequest accountDTO) {

        if (accountRepo.existsByEmail(accountDTO.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
        }

        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setEmail(accountDTO.getEmail());

        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

        account.setRole(AccountRole.USER);
        account.setStatus(true);

        return accountMapper.toAccountDTO(accountRepo.save(account));
    }


    @Override
    @Transactional
    public AccountDetailResponse updateAccount(String id, AccountUpdateRequest accountDTO) {
        Account existing = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!existing.getEmail().equals(accountDTO.getEmail()) && accountRepo.existsByEmail(accountDTO.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_ALREADY_EXISTS);
        }

        accountMapper.updateAccountFromRequest(accountDTO, existing);

        return accountMapper.toAccountDTO(accountRepo.save(existing));
    }

    @Override
    public Boolean deleteAccount(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.setStatus(false);
        accountRepo.save(account);
        return true;
    }
}