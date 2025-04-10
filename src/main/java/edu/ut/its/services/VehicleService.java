package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.VehicleMapper;
import edu.ut.its.models.dtos.VehicleDTO;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.services.impl.IVehicleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepo vehicleRepo;
    private final VehicleMapper vehicleMapper;

    @Override
    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
        Page<Vehicle> vehicles = vehicleRepo.findAll(pageable);

        if (vehicles.isEmpty()) {
            throw new DataNotFoundException("No vehicle found");
        }

        return vehicles.map(vehicleMapper::toVehicleDTO);
    }

    @Override
    public VehicleDTO getVehicleById(String id) {
        Vehicle vehicle = vehicleRepo.findById(id).orElseThrow(() -> new DataNotFoundException("vehicle not found"));
        return vehicleMapper.toVehicleDTO(vehicle);
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepo.findByLicensePlates(vehicleDTO.getLicensePlates());
        if (vehicle == null) {
            Vehicle newVehicle = vehicleRepo.save(vehicleMapper.toVehicle(vehicleDTO));
            return vehicleMapper.toVehicleDTO(newVehicle);
        }
        return vehicleMapper.toVehicleDTO(vehicle);
    }

    @Override
    public VehicleDTO updateVehicle(String id, VehicleDTO vehicleDTO) {
        Vehicle existing = vehicleRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        existing.setLicensePlates(vehicleDTO.getLicensePlates());
        existing.setType(vehicleDTO.getType());
        existing.setPriority(vehicleDTO.isPriority());

        return vehicleMapper.toVehicleDTO(vehicleRepo.save(existing));
    }
}