package edu.ut.its.models.dtos.requests;

import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraCreateRequest {
    private String streetId;

    @Builder.Default
    private boolean status = true;
}
