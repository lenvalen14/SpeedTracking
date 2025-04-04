package edu.ut.its.models.dtos;

import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLogDTO {
    private String streetLogId;
    private StreetDetailResponse street;
    private VehicleDTO vehicle;
    private float speedAverage;
    private int density;
    private int violationCount;
    private LocalDateTime createAt;
}
