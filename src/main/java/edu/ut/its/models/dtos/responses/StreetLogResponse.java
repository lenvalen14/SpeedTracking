package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import edu.ut.its.models.dtos.VehicleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLogResponse {
    private String streetLogId;
    private StreetDetailResponse street;
    private VehicleDTO vehicle;
    private float speedAverage;
    private int density;
    private int violationCount;
    private LocalDateTime createAt;
}
