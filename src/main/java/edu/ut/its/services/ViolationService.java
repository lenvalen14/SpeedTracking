package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.ViolationMapper;
import edu.ut.its.models.dtos.ViolationDTO;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.models.entities.Violation;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VehicleRepo;
import edu.ut.its.repositories.ViolationRepo;
import edu.ut.its.services.impl.IViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ViolationService implements IViolationService {

    @Autowired
    private ViolationRepo violationRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private ViolationMapper violationMapper;

    @Override
    public Page<ViolationDTO> getAllViolations(Pageable pageable) {

        Page<Violation> violation = violationRepo.findAll(pageable);

        if (violation.isEmpty()) {
            throw new DataNotFoundException("List of violations is empty");
        }

        return violation.map(violationMapper::toViolationDTO);
    }

    @Override
    public ViolationDTO getViolationById(String id) {
        Violation violation = violationRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Violation not found"));

        return violationMapper.toViolationDTO(violation);
    }

    @Override
    public ViolationDTO createViolation(ViolationDTO violationDTO) {

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        Street street = streetRepo.findById(violationDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        Violation violation = new Violation();

        violation.setVehicle(vehicle);
        violation.setStreet(street);
        violation.setSpeedRecorded(violationDTO.getSpeedRecorded());
        violation.setViolationLevel(violationDTO.getViolationLevel());
        violation.setEvidence(violationDTO.getEvidence());
        violation.setCreateAt(LocalDateTime.now());

        return violationMapper.toViolationDTO(violationRepo.save(violation));
    }

    @Override
    public ViolationDTO updateViolation(String id, ViolationDTO violationDTO) {

        Violation existingViolation = violationRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIOLATION_NOT_FOUND));

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        Street street = streetRepo.findById(violationDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        existingViolation.setVehicle(vehicle);
        existingViolation.setStreet(street);
        existingViolation.setSpeedRecorded(violationDTO.getSpeedRecorded());
        existingViolation.setViolationLevel(violationDTO.getViolationLevel());
        existingViolation.setEvidence(violationDTO.getEvidence());
        existingViolation.setCreateAt(LocalDateTime.now());

        return violationMapper.toViolationDTO(violationRepo.save(existingViolation));
    }
}