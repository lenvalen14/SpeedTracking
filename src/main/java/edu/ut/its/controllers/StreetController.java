package edu.ut.its.controllers;

import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.StreetService;
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
@RequestMapping("/street")
public class StreetController {

    @Autowired
    private StreetService streetService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<Page<StreetDetailResponse>>> getAllStreets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<StreetDetailResponse> responses = streetService.getAllStreets(pageable);

        ResponseWrapper<Page<StreetDetailResponse>> responseWrapper;

        if(responses != null && !responses.isEmpty())
        {
            responseWrapper = new ResponseWrapper<>("Data Street Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("List Data Of Street Is Empty", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<StreetDetailResponse>> getStreet(
            @PathVariable String requestID)
    {
        StreetDetailResponse responses = streetService.getStreetById(requestID);

        ResponseWrapper<StreetDetailResponse> responseWrapper;

        if(responses != null)
        {
            responseWrapper = new ResponseWrapper<>("Data Street Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("No Data Street Found", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ResponseWrapper<StreetDetailResponse>> createStreet(
            @Valid  @RequestBody StreetCreateRequest streetCreateRequest)
    {
        try {
            StreetDetailResponse streetDetailResponse = streetService.createStreet(streetCreateRequest);

            ResponseWrapper<StreetDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Create Street Successfully", streetDetailResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetDetailResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<StreetDetailResponse>> updateStreet(
            @Valid @RequestBody StreetDetailResponse streetDetailResponse,
            @PathVariable String requestID)
    {
        try {
            StreetDetailResponse response = streetService.updateStreet(requestID, streetDetailResponse);

            ResponseWrapper<StreetDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Update Street Successfully",response);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetDetailResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
