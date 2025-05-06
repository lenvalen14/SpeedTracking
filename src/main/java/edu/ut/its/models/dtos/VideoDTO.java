package edu.ut.its.models.dtos;

import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    private String videoId;
    private CameraDetailResponse camera;
    private String videoUrl;
}
