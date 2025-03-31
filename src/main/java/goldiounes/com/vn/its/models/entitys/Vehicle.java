package goldiounes.com.vn.its.models.entitys;

import goldiounes.com.vn.its.models.emuns.VehicleType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private String vehicleId;

    private String licensePlates;
    private VehicleType type;
    private boolean priority;
    private LocalDateTime createAt;
}
