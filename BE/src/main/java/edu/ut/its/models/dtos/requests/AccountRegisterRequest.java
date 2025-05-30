package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.enums.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "phone is required")
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Phone number must start with 0 and be exactly 10 digits"
    )
    private String phone;

    @NotBlank(message = "location is required")
    private String location;

    @NotBlank(message = "department is required")
    private String department;

    private AccountRole role;

    @Builder.Default
    private boolean status = true;

    private LocalDateTime createAt;
}
