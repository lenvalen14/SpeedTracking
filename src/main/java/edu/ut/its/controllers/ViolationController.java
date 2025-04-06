package edu.ut.its.controllers;

import edu.ut.its.models.dtos.ViolationDTO;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/violation")
public class ViolationController {

    @Autowired
    ViolationService violationService;

    @GetMapping()
    public ResponseEntity<ResponseWrapper<Page<ViolationDTO>>> getAllViolation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<ViolationDTO> violationDTOS = violationService.getAllViolations(pageable);

        if (violationDTOS != null && !violationDTOS.isEmpty()) {
            ResponseWrapper<Page<ViolationDTO>> responseWrapper =
                    new ResponseWrapper<>("Data Violations Successfully", violationDTOS);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            ResponseWrapper<Page<ViolationDTO>> responseWrapper =
                    new ResponseWrapper<>("List Data Of Violations Is Empty", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @GetMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<ViolationDTO>> getViolation(
            @PathVariable String requestID)
    {
        ViolationDTO violationDTO = violationService.getViolationById(requestID);

        if (violationDTO != null) {
            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>("Data Violations Successfully", violationDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>("Data Of Violations Not Found", null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseWrapper<ViolationDTO>> createViolation(
            @RequestBody ViolationDTO violationDTO)
    {
        try {
            ViolationDTO respDTO = violationService.createViolation(violationDTO);

            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>("Create Violations Successfully", respDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<ViolationDTO>> updateViolation(
            @RequestBody ViolationDTO violationDTO,
            @PathVariable String requestID)
    {
        try {
            ViolationDTO responseDTO = violationService.updateViolation(requestID, violationDTO);

            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>("Update Violations Successfully", responseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<ViolationDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
