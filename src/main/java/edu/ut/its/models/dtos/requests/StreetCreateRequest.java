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
public class StreetCreateRequest {
    @NotBlank(message = "Street name is required")
    private String name;
    private String area;
    private int speedLimit;
    private int cameraCount;
}
