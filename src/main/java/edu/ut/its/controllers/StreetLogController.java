package edu.ut.its.controllers;

import edu.ut.its.models.dtos.requests.StreetLogRequest;
import edu.ut.its.models.dtos.responses.StreetLogResponse;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.StreetLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/streetLog")
public class StreetLogController {

    @Autowired
    private StreetLogService streetLogService;

    @GetMapping()
    public ResponseEntity<ResponseWrapper<Page<StreetLogResponse>>> getAllStreetLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<StreetLogResponse> responses = streetLogService.getAllStreetLogs(pageable);

        ResponseWrapper<Page<StreetLogResponse>> responseWrapper;

        if(responses != null && !responses.isEmpty())
        {
            responseWrapper = new ResponseWrapper<>("Data StreetLog Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("List Data Of StreetLog Is Empty", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<StreetLogResponse>> getStreetLog(
            @PathVariable String requestID)
    {
        StreetLogResponse responses = streetLogService.getStreetLogById(requestID);

        ResponseWrapper<StreetLogResponse> responseWrapper;

        if(responses != null)
        {
            responseWrapper = new ResponseWrapper<>("Data StreetLog Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("No Data StreetLog Found", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseWrapper<StreetLogResponse>> createStreetLog(
            @Valid  @RequestBody StreetLogRequest streetLogDTO)
    {
        try {
            StreetLogResponse responses = streetLogService.createStreetLog(streetLogDTO);

            ResponseWrapper<StreetLogResponse> responseWrapper =
                    new ResponseWrapper<>("Create StreetLog Successfully", responses);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetLogResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<StreetLogResponse>> updateStreetLog(
            @Valid @RequestBody StreetLogResponse streetLogDTO,
            @PathVariable String requestID)
    {
        try {
            StreetLogResponse responses = streetLogService.updateStreetLog(requestID, streetLogDTO);

            ResponseWrapper<StreetLogResponse> responseWrapper =
                    new ResponseWrapper<>("Update StreetLog Successfully", responses);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetLogResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
