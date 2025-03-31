package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.AccountDTO;
import goldiounes.com.vn.its.models.emuns.AccountRole;
import goldiounes.com.vn.its.models.entitys.Account;
import goldiounes.com.vn.its.repositories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private Mapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<AccountDTO> getAllAccounts() {
        return mapper.convertToDtoList(accountRepo.findAllByStatusTrue(), AccountDTO.class);
    }

    @Override
    public Optional<AccountDTO> getAccountById(String id) {
        return accountRepo.findByAccountIdAndStatusTrue(id).map(acc -> mapper.convertToDto(acc, AccountDTO.class));
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

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

        return mapper.convertToDto(accountRepo.save(account), AccountDTO.class);
    }


    @Override
    public AccountDTO updateAccount(String id, AccountDTO accountDTO) {
        Account existing = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if (!existing.getEmail().equals(accountDTO.getEmail()) && accountRepo.existsByEmail(accountDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        existing.setName(accountDTO.getName());
        existing.setEmail(accountDTO.getEmail());

        existing.setRole(accountDTO.getRole());
        existing.setStatus(accountDTO.getStatus());

        return mapper.convertToDto(accountRepo.save(existing), AccountDTO.class);
    }

    @Override
    public void deleteAccount(String id) {
        Account account = accountRepo.findByAccountIdAndStatusTrue(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(false);
        accountRepo.save(account);
    }
}
