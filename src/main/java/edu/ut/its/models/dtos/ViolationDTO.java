package edu.ut.its.models.dtos;

import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.enums.ViolationLevel;
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
    private StreetDetailResponse street;
    private float speedRecorded;
    private ViolationLevel violationLevel;
    private String evidence;
    private LocalDateTime createAt;
}
