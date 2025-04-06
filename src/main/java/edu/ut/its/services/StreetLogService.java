package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mappers.StreetLogMapper;
import edu.ut.its.models.dtos.StreetLogDTO;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.services.impl.IStreetLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StreetLogService implements IStreetLogService {

    @Autowired
    private StreetLogRepo streetLogRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private StreetLogMapper streetLogMapper;

    @Override
    public List<StreetLogDTO> getAllStreetLogs() {
        List<StreetLogDTO> streetLogs = streetLogRepo.findAll()
                .stream()
                .map(streetLogMapper::toStreetLogDTO)
                .toList();
        if (streetLogs.isEmpty()) {
            throw new DataNotFoundException("No streets found");
        }
        return streetLogs;
    }

    @Override
    public StreetLogDTO getStreetLogById(String id) {
        StreetLog streetLog = streetLogRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Street Log Not Found"));
        return streetLogMapper.toStreetLogDTO(streetLog);
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

        return streetLogMapper.toStreetLogDTO(streetLogRepo.save(streetLog));
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

        return streetLogMapper.toStreetLogDTO(streetLogRepo.save(existing));
    }

    @Override
    public void deleteStreetLog(String id) {
        throw new UnsupportedOperationException("StreetLog cannot be deleted.");
    }

}
