package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mapper.VehicleMapper;
import edu.ut.its.models.dtos.VehicleDTO;
import edu.ut.its.models.entitys.Vehicle;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.services.impl.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<VehicleDTO> vehiclesDTO = vehicleRepo.findAll()
                .stream()
                .map(vehicleMapper::toVehicleDTO)
                .toList();
        if (vehiclesDTO.isEmpty()) {
            throw new DataNotFoundException("No vehicle found");
        }
        return vehiclesDTO;
    }

    @Override
    public VehicleDTO getVehicleById(String id) {
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new DataNotFoundException("vehicle not found"));
        return vehicleMapper.toVehicleDTO(vehicle);
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepo.existsVehicleByLicensePlates(vehicleDTO.getLicensePlates());
        if (vehicle == null) {
            Vehicle newVehicle = vehicleRepo.save(vehicleMapper.toVehicle(vehicleDTO));
            return vehicleMapper.toVehicleDTO(newVehicle);
        }
        return vehicleMapper.toVehicleDTO(vehicle);
    }

    @Override
    public VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO) {
        Vehicle existing = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setLicensePlates(vehicleDTO.getLicensePlates());
        existing.setType(vehicleDTO.getType());
        existing.setPriority(vehicleDTO.isPriority());

        return vehicleMapper.toVehicleDTO(vehicleRepo.save(existing));
    }
}
