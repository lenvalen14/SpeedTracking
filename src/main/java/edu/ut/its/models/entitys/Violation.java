package edu.ut.its.models.entitys;

import edu.ut.its.models.emuns.ViolationLevel;
import lombok.*;
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

    private float speedRecorded;
    private ViolationLevel violationLevel;
    private String evidence;
    private LocalDateTime createAt;
}
