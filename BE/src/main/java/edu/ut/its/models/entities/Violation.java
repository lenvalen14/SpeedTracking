package edu.ut.its.models.entities;

import edu.ut.its.models.enums.ViolationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "violations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Violation {
    @Id
    private String violationId;

    @DBRef
    private Vehicle vehicle;

    @DBRef
    private Street street;

    private float speed;
    private String evidence;
    private LocalDateTime createAt;
}
