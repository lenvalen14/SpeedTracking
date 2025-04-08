package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.enums.ViolationLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViolationRequest {
    private String violationId;
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;
    @NotBlank(message = "Street ID is required")
    private String streetId;
    private float speedRecorded;
    private ViolationLevel violationLevel;
    private String evidence;
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
}
