package goldiounes.com.vn.its.models.entitys;

import goldiounes.com.vn.its.models.emuns.AccountRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String accountId;

    private String name;
    private String email;
    private String password;
    private AccountRole role;
    private boolean status;
    private LocalDateTime createAt;
}

