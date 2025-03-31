package goldiounes.com.vn.its.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDTO {
    private String cameraId;
    private StreetDTO street;
    private boolean status;
}