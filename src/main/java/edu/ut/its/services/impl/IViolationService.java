package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.ViolationDTO;

import java.util.List;
import java.util.Optional;

public interface IViolationService {
    List<ViolationDTO> getAllViolations();
    Optional<ViolationDTO> getViolationById(String id);
    ViolationDTO createViolation(ViolationDTO violationDTO);
    ViolationDTO updateViolation(String id, ViolationDTO violationDTO);
}
