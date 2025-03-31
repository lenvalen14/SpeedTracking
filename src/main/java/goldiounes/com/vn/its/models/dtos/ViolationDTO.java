package goldiounes.com.vn.its.models.dtos;

import goldiounes.com.vn.its.models.emuns.ViolationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViolationDTO {
    private String violationId;
    private VehicleDTO vehicle;
    private StreetDTO street;
    private float speedRecorded;
    private ViolationLevel violationLevel;
    private String evidence;
    private LocalDateTime createAt;
}
