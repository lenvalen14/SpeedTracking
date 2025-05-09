package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.enums.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {

    @NotBlank(message = "Account ID is required")
    private String accountId;

    private String name;

    @NotBlank(message = "phone is required")
    @Size(min = 10, message = "phone must be at least 8 characters")
    private String phone;

    @NotBlank(message = "location is required")
    private String location;

    @NotBlank(message = "department is required")
    private String department;

    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private AccountRole role;
    private Boolean status;
}
