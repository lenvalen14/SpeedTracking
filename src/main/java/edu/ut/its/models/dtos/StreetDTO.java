package edu.ut.its.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetDTO {
    private String streetId;
    private String name;
    private String area;
    private int speedLimit;
    private int cameraCount;
}
