package edu.ut.its.services;

import edu.ut.its.models.dtos.StreetLogDTO;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.models.entitys.StreetLog;
import edu.ut.its.models.entitys.Vehicle;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StreetLogService implements IStreetLogService {

    @Autowired
    private StreetLogRepo streetLogRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private Mapper mapper;

    @Override
    public List<StreetLogDTO> getAllStreetLogs() {
        return mapper.convertToDtoList(streetLogRepo.findAll(), StreetLogDTO.class);
    }

    @Override
    public Optional<StreetLogDTO> getStreetLogById(String id) {
        return streetLogRepo.findById(id).map(log -> mapper.convertToDto(log, StreetLogDTO.class));
    }

    @Override
    public StreetLogDTO createStreetLog(StreetLogDTO streetLogDTO) {

        Street street = streetRepo.findById(streetLogDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

        Vehicle vehicle = vehicleRepo.findById(streetLogDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        StreetLog streetLog = new StreetLog();
        streetLog.setStreet(street);
        streetLog.setVehicle(vehicle);
        streetLog.setSpeedAverage(streetLogDTO.getSpeedAverage());
        streetLog.setDensity(streetLogDTO.getDensity());
        streetLog.setViolationCount(streetLogDTO.getViolationCount());

        return mapper.convertToDto(streetLogRepo.save(streetLog), StreetLogDTO.class);
    }

    @Override
    public StreetLogDTO updateStreetLog(String id, StreetLogDTO streetLogDTO) {
        StreetLog existing = streetLogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("StreetLog not found"));

        Street street = streetRepo.findById(streetLogDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

        Vehicle vehicle = vehicleRepo.findById(streetLogDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setStreet(street);
        existing.setVehicle(vehicle);
        existing.setSpeedAverage(streetLogDTO.getSpeedAverage());
        existing.setDensity(streetLogDTO.getDensity());
        existing.setViolationCount(streetLogDTO.getViolationCount());

        return mapper.convertToDto(streetLogRepo.save(existing), StreetLogDTO.class);
    }

    @Override
    public void deleteStreetLog(String id) {
        throw new UnsupportedOperationException("StreetLog cannot be deleted.");
    }

}
