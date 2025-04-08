package edu.ut.its.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.ut.its.models.dtos.requests.ViolationRequest;
import edu.ut.its.models.dtos.responses.ViolationResponse;


public interface IViolationService {
    Page<ViolationResponse> getAllViolations(Pageable pageable);
    ViolationResponse getViolationById(String id);
    ViolationResponse createViolation(ViolationRequest violationDTO);
    ViolationResponse updateViolation(String id, ViolationResponse violationDTO);
}
