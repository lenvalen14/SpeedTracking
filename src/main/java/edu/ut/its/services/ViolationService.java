package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<ViolationDTO> getAllViolations() {
        List<ViolationDTO> violationDTOS = violationRepo.findAll()
                .stream()
                .map(violationMapper::toViolationDTO)
                .toList();

        if (violationDTOS.isEmpty()) {
            throw new DataNotFoundException("List of violations is empty");
        }

        return violationDTOS;
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
