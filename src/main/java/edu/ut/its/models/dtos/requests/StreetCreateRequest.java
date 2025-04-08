package edu.ut.its.models.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetCreateRequest {
    @NotBlank(message = "Street name is required")
    private String name;
    private String area;

    @Min(value = 0, message = "Speed limit must be at least 0")
    @Max(value = 300, message = "Speed limit must be not exceed 300")
    private int speedLimit;

    @Min(value = 0, message = "Camera count must be at least 0")
    private int cameraCount;
}
