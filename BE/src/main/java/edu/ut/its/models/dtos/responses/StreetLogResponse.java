package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import edu.ut.its.models.dtos.VehicleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLogResponse {
    private String streetLogId;
    private StreetDetailResponse street;
    private List<VehicleDTO> vehicles;
    private float speedAvg;
    private int vehiclesCount;
    private int violatorsCount;
    private LocalDateTime createAt;
}
