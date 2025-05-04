package edu.ut.its.controllers;

import edu.ut.its.models.dtos.requests.ViolationRequest;
import edu.ut.its.models.dtos.responses.ViolationResponse;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.ViolationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/violation")
public class ViolationController {

    @Autowired
    ViolationService violationService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<Page<ViolationResponse>>> getAllViolation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViolationResponse> violationDTOS = violationService.getAllViolations(pageable);

        if (violationDTOS != null && !violationDTOS.isEmpty()) {
            ResponseWrapper<Page<ViolationResponse>> responseWrapper =
                    new ResponseWrapper<>("Data Violations Successfully", violationDTOS);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            ResponseWrapper<Page<ViolationResponse>> responseWrapper =
                    new ResponseWrapper<>("List Data Of Violations Is Empty", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @GetMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<ViolationResponse>> getViolation(
            @PathVariable String requestID)
    {
        ViolationResponse violationDTO = violationService.getViolationById(requestID);

        if (violationDTO != null) {
            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>("Data Violations Successfully", violationDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>("Data Of Violations Not Found", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<ViolationResponse>> createViolation(
            @Valid  @RequestBody ViolationRequest violationDTO)
    {
        try {
            ViolationResponse respDTO = violationService.createViolation(violationDTO);

            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>("Create Violations Successfully", respDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<ViolationResponse>> updateViolation(
            @Valid @RequestBody ViolationResponse violationDTO,
            @PathVariable String requestID)
    {
        try {
            ViolationResponse responseDTO = violationService.updateViolation(requestID, violationDTO);

            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>("Update Violations Successfully", responseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<ViolationResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
