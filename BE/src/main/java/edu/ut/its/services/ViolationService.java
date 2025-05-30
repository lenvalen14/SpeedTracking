package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.ViolationMapper;
import edu.ut.its.models.dtos.requests.ViolationRequest;
import edu.ut.its.models.dtos.responses.ViolationResponse;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.models.entities.Violation;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.repositories.ViolationRepo;
import edu.ut.its.services.impl.IViolationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ViolationService implements IViolationService {

    private final ViolationRepo violationRepo;
    private final VehicleRepo vehicleRepo;
    private final StreetRepo streetRepo;
    private final ViolationMapper violationMapper;

    @Override
    public Page<ViolationResponse> getAllViolations(Pageable pageable) {

        Page<Violation> violation = violationRepo.findAll(pageable);

        if (violation.isEmpty()) {
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }

        return violation.map(violationMapper::toViolationDTO);
    }

    @Override
    public ViolationResponse getViolationById(String id) {
        Violation violation = violationRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        return violationMapper.toViolationDTO(violation);
    }

    @Override
    public ViolationResponse createViolation(ViolationRequest violationDTO) {

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        Street street = streetRepo.findById(violationDTO.getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        Violation violation = new Violation();

        violation.setVehicle(vehicle);
        violation.setStreet(street);
        violation.setSpeed(violationDTO.getSpeed());
        violation.setEvidence(violationDTO.getEvidence());
        violation.setCreateAt(LocalDateTime.now());

        return violationMapper.toViolationDTO(violationRepo.save(violation));
    }

    @Override
    public ViolationResponse updateViolation(String id, ViolationResponse violationDTO) {

        Violation existingViolation = violationRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIOLATION_NOT_FOUND));

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        Street street = streetRepo.findById(violationDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        existingViolation.setVehicle(vehicle);
        existingViolation.setStreet(street);
        existingViolation.setSpeed(violationDTO.getSpeed());
        existingViolation.setEvidence(violationDTO.getEvidence());
        existingViolation.setCreateAt(LocalDateTime.now());

        return violationMapper.toViolationDTO(violationRepo.save(existingViolation));
    }
}