package edu.ut.its.configuration;

import edu.ut.its.models.entities.Account;
import edu.ut.its.models.enums.AccountRole;
import edu.ut.its.repositories.AccountRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${admin-password}")
    protected String adminPassword;

    @NonFinal
    @Value("${admin-email}")
    protected String adminEmail;

    @NonFinal
    @Value("${admin-name}")
    protected String adminName;

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(AccountRepo accountRepo) {
        return args -> {
            if(accountRepo.findByEmail(adminEmail) == null){
                Account account = new Account();
                account.setName(adminName);
                account.setEmail(adminEmail);
                account.setPassword(passwordEncoder.encode(adminPassword));
                account.setRole(AccountRole.ADMIN);
                account.setStatus(true);
                account.setCreateAt(LocalDateTime.now());
                accountRepo.save(account);
                log.warn("Admin account created with default password, please change it!");
            }
        };
    }
}
