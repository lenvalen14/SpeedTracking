package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.StreetLogMapper;
import edu.ut.its.models.dtos.VehicleDTO;
import edu.ut.its.models.dtos.requests.StreetLogRequest;
import edu.ut.its.models.dtos.requests.VehicleRequestJsonAI;
import edu.ut.its.models.dtos.requests.ViolatorRequestJsonAI;
import edu.ut.its.models.dtos.responses.StreetLogResponse;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.models.entities.Violation;
import edu.ut.its.models.enums.VehicleType;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.repositories.ViolationRepo;
import edu.ut.its.services.impl.IStreetLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StreetLogService implements IStreetLogService {

    private final StreetLogRepo streetLogRepo;
    private final VehicleRepo vehicleRepo;
    private final StreetRepo streetRepo;
    private final StreetLogMapper streetLogMapper;
    private final ViolationRepo violationRepo;

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
    public StreetLogResponse createStreetLogFromJson(StreetLogRequest streetLogDTO) {

        Street street = streetRepo.findById(streetLogDTO.getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_LOG_NOT_FOUND));

        List<Vehicle> createdVehicles = new ArrayList<>();

        for (VehicleRequestJsonAI v : streetLogDTO.getVehicles()) {
            Vehicle vehicleToAdd = new Vehicle();
            vehicleToAdd.setLicensePlates(v.getLicense_plate());
            vehicleToAdd.setCreateAt(LocalDateTime.now());
            try {
                vehicleToAdd.setType(VehicleType.valueOf(v.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.VEHICLE_INVALID_TYPE);
            }
            createdVehicles.add(vehicleRepo.save(vehicleToAdd));
        }

        StreetLog streetLog = new StreetLog();
        streetLog.setStreet(street);
        streetLog.setVehicles(createdVehicles);
        streetLog.setSpeedAvg(streetLogDTO.getSpeedAvg());
        streetLog.setDensity((double) streetLogDTO.getVehiclesCount() / 250);
        streetLog.setVehiclesCount(streetLogDTO.getVehiclesCount());
        streetLog.setViolatorsCount(streetLogDTO.getViolatorsCount());
        streetLog.setCreateAt(LocalDateTime.now());

        streetLog = streetLogRepo.save(streetLog);

        for (ViolatorRequestJsonAI violator : streetLogDTO.getViolators()) {
            Vehicle violatedVehicle = null;

            for (Vehicle v : createdVehicles) {
                if (v.getLicensePlates().equals(violator.getLicense_plate())) {
                    violatedVehicle = v;
                    break;
                }
            }

            if (violatedVehicle != null) {
                Violation violation = new Violation();
                violation.setVehicle(violatedVehicle);
                violation.setStreet(street);
                violation.setSpeed(violator.getSpeed());
                violation.setEvidence(violator.getImage_url());
                violation.setCreateAt(LocalDateTime.now());

                violationRepo.save(violation);
            }
        }

        return streetLogMapper.toStreetLogDTO(streetLog);
    }


    @Override
    public StreetLogResponse updateStreetLog(String id, StreetLogResponse streetLogDTO) {
        StreetLog existing = streetLogRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STREET_LOG_NOT_FOUND));

        Street street = streetRepo.findById(streetLogDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        List<Vehicle> updatedVehicles = new ArrayList<>();
        for (VehicleDTO vehicleRequest : streetLogDTO.getVehicles()) {
            Vehicle vehicleToUpdate = vehicleRepo.findByLicensePlates(vehicleRequest.getLicensePlates());

            if (vehicleToUpdate == null) {
                vehicleToUpdate = new Vehicle();
                vehicleToUpdate.setLicensePlates(vehicleRequest.getLicensePlates());
                vehicleToUpdate.setType(vehicleRequest.getType());
                vehicleToUpdate.setCreateAt(LocalDateTime.now());
                updatedVehicles.add(vehicleRepo.save(vehicleToUpdate));
            } else {
                vehicleToUpdate.setType(vehicleRequest.getType());
                updatedVehicles.add(vehicleRepo.save(vehicleToUpdate));
            }
        }

        existing.setStreet(street);
        existing.setVehicles(updatedVehicles);
        existing.setSpeedAvg(streetLogDTO.getSpeedAvg());
        existing.setSpeedAvg(streetLogDTO.getSpeedAvg());
        existing.setVehiclesCount(streetLogDTO.getVehiclesCount());
        existing.setViolatorsCount(streetLogDTO.getViolatorsCount());
        existing.setDensity((double) streetLogDTO.getVehiclesCount() / 250);

        return streetLogMapper.toStreetLogDTO(streetLogRepo.save(existing));
    }

}