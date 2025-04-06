package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.ViolationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IViolationService {
    Page<ViolationDTO> getAllViolations(Pageable pageable);
    ViolationDTO getViolationById(String id);
    ViolationDTO createViolation(ViolationDTO violationDTO);
    ViolationDTO updateViolation(String id, ViolationDTO violationDTO);
}
