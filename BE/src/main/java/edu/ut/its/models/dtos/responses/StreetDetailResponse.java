package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetDetailResponse {
    private String streetId;
    private String name;
    private String area;
    private int speedLimit;
    private int cameraCount;
    private String latitude;
    private String longitude;
    private List<CameraDetailResponse> cameraDetail;
}
