package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mapper.AccountMapper;
import edu.ut.its.mapper.ViolationMapper;
import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.emuns.AccountRole;
import edu.ut.its.models.entitys.Account;
import edu.ut.its.repositories.AccountRepo;
import edu.ut.its.services.impl.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        Page<AccountDetailResponse> responses = accounts.map(accountMapper::toAccountDTO);

        return responses;
    }

    @Override
    public AccountDetailResponse getAccountById(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return accountMapper.toAccountDTO(account);
    }

    @Override
    public AccountDetailResponse createAccount(AccountRegisterRequest accountDTO) {

        if (accountRepo.existsByEmail(accountDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setEmail(accountDTO.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

        account.setRole(AccountRole.USER);
        account.setStatus(true);

        return accountMapper.toAccountDTO(accountRepo.save(account));
    }


    @Override
    public AccountDetailResponse updateAccount(String id, AccountUpdateRequest accountDTO) {
        Account existing = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if (!existing.getEmail().equals(accountDTO.getEmail()) && accountRepo.existsByEmail(accountDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        accountMapper.updateAccountFromRequest(accountDTO, existing);

        return accountMapper.toAccountDTO(accountRepo.save(existing));
    }

    @Override
    public Boolean deleteAccount(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(false);
        accountRepo.save(account);
        return true;
    }
}
