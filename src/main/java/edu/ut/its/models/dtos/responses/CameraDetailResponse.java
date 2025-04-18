package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDetailResponse {
    private String cameraId;
//    private StreetDetailResponse street;
    private boolean status;
    private String videoUrl;
}