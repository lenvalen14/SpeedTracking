package edu.ut.its.models.dtos.responses;

import edu.ut.its.models.dtos.StreetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDetailResponse {
    private String cameraId;
    private StreetDTO street;
    private boolean status;
}