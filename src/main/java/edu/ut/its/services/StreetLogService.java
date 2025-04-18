package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.StreetLogMapper;
import edu.ut.its.models.dtos.requests.StreetLogRequest;
import edu.ut.its.models.dtos.responses.StreetLogResponse;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.services.impl.IStreetLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StreetLogService implements IStreetLogService {

    private final StreetLogRepo streetLogRepo;
    private final VehicleRepo vehicleRepo;
    private final StreetRepo streetRepo;
    private final StreetLogMapper streetLogMapper;

    @Override
    public Page<StreetLogResponse> getAllStreetLogs(Pageable pageable) {
        Page<StreetLog> streetLogs = streetLogRepo.findAll(pageable);

        if (streetLogs.isEmpty()) {
            throw new AppException(ErrorCode.STREET_LOG_NOT_FOUND);
        }

        return streetLogs.map(streetLogMapper::toStreetLogDTO);
    }

    @Override
    public StreetLogResponse getStreetLogById(String id) {
        StreetLog streetLog = streetLogRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.STREET_LOG_NOT_FOUND));
        return streetLogMapper.toStreetLogDTO(streetLog);
    }

    @Override
    public StreetLogResponse createStreetLog(StreetLogRequest streetLogDTO) {

        Street street = streetRepo.findById(streetLogDTO.getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_LOG_NOT_FOUND));

        Vehicle vehicle = vehicleRepo.findById(streetLogDTO.getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        StreetLog streetLog = new StreetLog();
        streetLog.setStreet(street);
        streetLog.setVehicle(vehicle);
        streetLog.setSpeedAverage(streetLogDTO.getSpeedAverage());
        streetLog.setDensity(streetLogDTO.getDensity());
        streetLog.setViolationCount(streetLogDTO.getViolationCount());

        return streetLogMapper.toStreetLogDTO(streetLogRepo.save(streetLog));
    }

    @Override
    public StreetLogResponse updateStreetLog(String id, StreetLogResponse streetLogDTO) {
        StreetLog existing = streetLogRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STREET_LOG_NOT_FOUND));

        Street street = streetRepo.findById(streetLogDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        Vehicle vehicle = vehicleRepo.findById(streetLogDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        existing.setStreet(street);
        existing.setVehicle(vehicle);
        existing.setSpeedAverage(streetLogDTO.getSpeedAverage());
        existing.setDensity(streetLogDTO.getDensity());
        existing.setViolationCount(streetLogDTO.getViolationCount());

        return streetLogMapper.toStreetLogDTO(streetLogRepo.save(existing));
    }
}