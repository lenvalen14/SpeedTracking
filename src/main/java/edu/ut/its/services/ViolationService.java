package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mapper.ViolationMapper;
import edu.ut.its.models.dtos.ViolationDTO;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.models.entitys.Vehicle;
import edu.ut.its.models.entitys.Violation;
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

        Page<ViolationDTO> response = violation.map(violationMapper::toViolationDTO);

        return response;
    }

    @Override
    public ViolationDTO getViolationById(String id) {
        Violation violation = violationRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Violation not found"));

        return violationMapper.toViolationDTO(violation);
    }

    @Override
    public ViolationDTO createViolation(ViolationDTO violationDTO) {

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Street street = streetRepo.findById(violationDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

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
                .orElseThrow(() -> new RuntimeException("Violation not found"));

        Vehicle vehicle = vehicleRepo.findById(violationDTO.getVehicle().getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Street street = streetRepo.findById(violationDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

        existingViolation.setVehicle(vehicle);
        existingViolation.setStreet(street);
        existingViolation.setSpeedRecorded(violationDTO.getSpeedRecorded());
        existingViolation.setViolationLevel(violationDTO.getViolationLevel());
        existingViolation.setEvidence(violationDTO.getEvidence());
        existingViolation.setCreateAt(LocalDateTime.now());

        return violationMapper.toViolationDTO(violationRepo.save(existingViolation));
    }
}
