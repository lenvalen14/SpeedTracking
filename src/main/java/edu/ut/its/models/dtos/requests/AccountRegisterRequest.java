package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.emuns.AccountRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterRequest {

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")

    private String password;
    private AccountRole role;
    private Boolean status;
    private LocalDateTime createAt;
}
