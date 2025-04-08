package edu.ut.its.models.dtos.requests;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreetLogRequest {
    private String streetLogId;
    @NotBlank(message = "Street ID is required")
    private String streetId;
    @NotBlank(message = "Camera ID is required")
    private String cameraId;
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;
    @Min(value = 0, message = "Speed recorded must be greater than or equal to 0")
    @Max(value = 300, message = "Speed recorded must be less than or equal to 300")
    private float speedAverage;
    @Min(value = 0, message = "Density must be greater than or equal to 0")
    private int density;
    @Min(value = 0, message = "Violation count must be greater than or equal to 0")
    private int violationCount;
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
}
