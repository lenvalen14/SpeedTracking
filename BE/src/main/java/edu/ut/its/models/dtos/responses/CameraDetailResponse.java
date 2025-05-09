package edu.ut.its.models.dtos.responses;

import edu.ut.its.models.dtos.VideoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDetailResponse {
    private String cameraId;
    private StreetDetailResponse street;
    private boolean status;
    private List<VideoDTO> video;
}