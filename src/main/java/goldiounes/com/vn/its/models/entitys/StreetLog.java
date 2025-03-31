package goldiounes.com.vn.its.models.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "street_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLog {
    @Id
    private String streetLogId;

    @DBRef
    private Street street;   // Tham chiếu đến Street

    @DBRef
    private Vehicle vehicle; // Tham chiếu đến Vehicle (nếu cần)

    private float speedAverage;
    private int density;
    private int violationCount;
    private LocalDateTime createAt;
}
