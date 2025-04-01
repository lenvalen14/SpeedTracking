package edu.ut.its.models.dtos;

import edu.ut.its.models.emuns.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private String vehicleId;
    private String licensePlates;
    private VehicleType type;
    private boolean priority;
    private LocalDateTime createAt;
}

