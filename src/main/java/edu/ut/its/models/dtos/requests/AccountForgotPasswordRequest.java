package edu.ut.its.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountForgotPasswordRequest {
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Retyped password is required")
    private String retypedPassword;
}
