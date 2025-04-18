package edu.ut.its.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestJsonAI {
    private int tracker_id;
    private float speed;
    private String type;
    private String license_plate;
}