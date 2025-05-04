package edu.ut.its.controllers;

import edu.ut.its.models.dtos.VehicleDTO;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.VehicleService;
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
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<Page<VehicleDTO>>> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleDTO> responses = vehicleService.getAllVehicles(pageable);

        ResponseWrapper<Page<VehicleDTO>> responseWrapper;

        if (responses != null && !responses.isEmpty()) {
            responseWrapper = new ResponseWrapper<>("Data Vehicle Successfully",responses);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            responseWrapper = new ResponseWrapper<>("No Data Vehicle Found",null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @GetMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<VehicleDTO>> getVehicle(
            @PathVariable String requestID)
    {
        VehicleDTO vehicleDTO = vehicleService.getVehicleById(requestID);

        ResponseWrapper<VehicleDTO> responseWrapper;

        if (vehicleDTO != null) {
            responseWrapper = new ResponseWrapper<>("Data Vehicle Successfully",vehicleDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            responseWrapper = new ResponseWrapper<>("No Data Vehicle Found",null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<VehicleDTO>> createVehicle(
            @Valid  @RequestBody VehicleDTO vehicleDTO)
    {
        try {
            VehicleDTO responseDTO = vehicleService.createVehicle(vehicleDTO);

            ResponseWrapper<VehicleDTO> responseWrapper =
                    new ResponseWrapper<>("Create Vehicle Successfully",responseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<VehicleDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(),null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<VehicleDTO>> updateVehicle(
            @Valid @RequestBody VehicleDTO vehicleDTO,
            @PathVariable String requestID)
    {
        try {
            VehicleDTO responseDTO = vehicleService.updateVehicle(requestID, vehicleDTO);

            ResponseWrapper<VehicleDTO> responseWrapper =
                    new ResponseWrapper<>("Update Vehicle Successfully",responseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<VehicleDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(),null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
