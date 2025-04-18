package edu.ut.its.models.dtos;

import edu.ut.its.models.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private String vehicleId;
    private String licensePlates;
    private VehicleType type;
    private LocalDateTime createAt;
}