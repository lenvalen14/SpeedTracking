package edu.ut.its.models.dtos;

import edu.ut.its.models.enums.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenDTO {
    private String tokenId;
    private String token;
    private String refreshToken;
    private String tokenType;
    private LocalDateTime expirationDate;
    private LocalDateTime refreshExpirationDate;
    private boolean revoked;
    private boolean expired;

    private String accountId;
    private String email;

    private AccountRole role;
}
