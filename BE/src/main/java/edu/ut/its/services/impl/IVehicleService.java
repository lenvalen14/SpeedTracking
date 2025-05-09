package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.VehicleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVehicleService {
    Page<VehicleDTO> getAllVehicles(Pageable pageable);
    VehicleDTO getVehicleById(String id);
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);
    VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO);
}
