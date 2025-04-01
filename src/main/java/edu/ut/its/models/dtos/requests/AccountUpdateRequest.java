package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.emuns.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    private String accountId;
    private String name;
    private String email;
    private String password;
    private AccountRole role;
    private Boolean status;
    private LocalDateTime createAt;
}
