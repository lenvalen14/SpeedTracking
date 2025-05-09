package edu.ut.its.services;

import edu.ut.its.components.JwtTokenUtils;
import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.AccountMapper;
import edu.ut.its.models.dtos.requests.AccountForgotPasswordRequest;
import edu.ut.its.models.dtos.requests.AccountLoginRequest;
import edu.ut.its.models.dtos.requests.AccountRegisterRequest;
import edu.ut.its.models.dtos.requests.AccountUpdateRequest;
import edu.ut.its.models.dtos.responses.AccountDetailResponse;
import edu.ut.its.models.enums.AccountRole;
import edu.ut.its.models.entities.Account;
import edu.ut.its.repositories.AccountRepo;
import edu.ut.its.services.impl.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepo accountRepo;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Page<AccountDetailResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepo.findAllByStatusTrue(pageable);

        if (accounts.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
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
        account.setPhone(accountDTO.getPhone());
        account.setLocation(accountDTO.getLocation());
        account.setDepartment(accountDTO.getDepartment());

        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

        if(accountDTO.getRole() == null) {
            accountDTO.setRole(AccountRole.OPERATOR);
        }

        account.setRole(accountDTO.getRole());
        account.setStatus(true);
        account.setCreateAt(LocalDateTime.now());

        return accountMapper.toAccountDTO(accountRepo.save(account));
    }

    @Override
    public String login(AccountLoginRequest accountLoginRequest) {
        Account existingAccount = accountRepo.findByEmail(accountLoginRequest.getEmail());
        if (existingAccount == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        if (!passwordEncoder.matches(accountLoginRequest.getPassword(), existingAccount.getPassword())) {
            throw new AppException(ErrorCode.ACCOUNT_PASSWORD_INVALID);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                accountLoginRequest.getEmail(), accountLoginRequest.getPassword(),
                existingAccount.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingAccount);
    }

    @Override
    public Account getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        }
        String email = jwtTokenUtils.extractEmail(token);
        Account account = accountRepo.findByEmail(email);
        if (account != null) {
            return account;
        } else {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
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
    public AccountDetailResponse forgotPassword(String email, AccountForgotPasswordRequest request){
        Account account = accountRepo.findByEmail(email);

        if(account == null)
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);

        if(!request.getPassword().equals(request.getRetypedPassword()))
            throw new AppException(ErrorCode.ACCOUNT_PASSWORD_CONFIRM_NOT_MATCH);

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepo.save(account);
        return accountMapper.toAccountDTO(accountRepo.save(account));
    }

    @Override
    public Boolean deleteAccount(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.setStatus(false);
        accountRepo.save(account);
        return true;
    }
}