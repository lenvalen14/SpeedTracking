package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.dtos.StreetDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraUpdateRequest {
    @NotBlank(message = "Camera ID is required")
    private String cameraId;
    private StreetDTO street;
    private Boolean status;
}
