package edu.ut.its.services;

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
        return mapper.convertToDtoList(vehicleRepo.findAll(), VehicleDTO.class);
    }

    @Override
    public Optional<VehicleDTO> getVehicleById(String id) {
        return vehicleRepo.findById(id)
                .map(v -> mapper.convertToDto(v, VehicleDTO.class));
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = mapper.convertToEntity(vehicleDTO, Vehicle.class);
        return mapper.convertToDto(vehicleRepo.save(vehicle), VehicleDTO.class);
    }

    @Override
    public VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO) {
        Vehicle existing = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setLicensePlates(vehicleDTO.getLicensePlates());
        existing.setType(vehicleDTO.getType());
        existing.setPriority(vehicleDTO.isPriority());

        return mapper.convertToDto(vehicleRepo.save(existing), VehicleDTO.class);
    }
}
