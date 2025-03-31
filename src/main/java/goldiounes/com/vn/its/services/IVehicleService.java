package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.VehicleDTO;
import java.util.List;
import java.util.Optional;

public interface IVehicleService {
    List<VehicleDTO> getAllVehicles();
    Optional<VehicleDTO> getVehicleById(String id);
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);
    VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO);
}
