package goldiounes.com.vn.its.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLogDTO {
    private String streetLogId;
    private StreetDTO street;
    private VehicleDTO vehicle;
    private float speedAverage;
    private int density;
    private int violationCount;
    private LocalDateTime createAt;
}
