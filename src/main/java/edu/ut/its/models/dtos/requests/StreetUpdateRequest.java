package edu.ut.its.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetUpdateRequest {
    @NotBlank(message = "Street ID is required")
    private String streetId;
    private String name;
    private String area;
    private int speedLimit;
    private int cameraCount;
}
