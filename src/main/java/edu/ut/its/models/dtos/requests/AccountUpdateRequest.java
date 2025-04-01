package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.emuns.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {

    @NotBlank(message = "Account ID is required")
    private String accountId;

    private String name;

    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private AccountRole role;
    private Boolean status;
}
