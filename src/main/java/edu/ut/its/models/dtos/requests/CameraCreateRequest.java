package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.dtos.StreetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraCreateRequest {
    private StreetDTO street;
    private boolean status;
}
