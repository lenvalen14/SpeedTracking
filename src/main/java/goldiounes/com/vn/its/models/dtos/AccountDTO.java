package goldiounes.com.vn.its.models.dtos;

import goldiounes.com.vn.its.models.emuns.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String accountId;
    private String name;
    private String email;
    private AccountRole role;
    private Boolean status;
    private LocalDateTime createAt;
}