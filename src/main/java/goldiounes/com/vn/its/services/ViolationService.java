package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.ViolationDTO;
import goldiounes.com.vn.its.models.entitys.Violation;
import goldiounes.com.vn.its.models.entitys.Vehicle;
import goldiounes.com.vn.its.models.entitys.Street;
import goldiounes.com.vn.its.repositories.ViolationRepo;
import goldiounes.com.vn.its.repositories.VehicleRepo;
import goldiounes.com.vn.its.repositories.StreetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ViolationService implements IViolationService {

    @Autowired
    private ViolationRepo violationRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private Mapper mapper;

    @Override
    public List<ViolationDTO> getAllViolations() {
        return mapper.convertToDtoList(violationRepo.findAll(), ViolationDTO.class);
    }

    @Override
    public Optional<ViolationDTO> getViolationById(String id) {
        return violationRepo.findById(id)
                .map(v -> mapper.convertToDto(v, ViolationDTO.class));
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

        return mapper.convertToDto(violationRepo.save(violation), ViolationDTO.class);
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

        return mapper.convertToDto(violationRepo.save(existingViolation), ViolationDTO.class);
    }
}
