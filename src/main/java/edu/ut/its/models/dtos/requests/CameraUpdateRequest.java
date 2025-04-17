package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.dtos.responses.StreetDetailResponse;
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
    private StreetDetailResponse street;
    private Boolean status;
    private String videoUrl;
}
