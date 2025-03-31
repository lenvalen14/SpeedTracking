package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.ViolationDTO;
import java.util.List;
import java.util.Optional;

public interface IViolationService {
    List<ViolationDTO> getAllViolations();
    Optional<ViolationDTO> getViolationById(String id);
    ViolationDTO createViolation(ViolationDTO violationDTO);
    ViolationDTO updateViolation(String id, ViolationDTO violationDTO);
}
