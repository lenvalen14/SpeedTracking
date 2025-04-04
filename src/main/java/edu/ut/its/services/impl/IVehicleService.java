package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.VehicleDTO;

import java.util.List;
import java.util.Optional;

public interface IVehicleService {
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleById(String id);
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);
    VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO);
}
