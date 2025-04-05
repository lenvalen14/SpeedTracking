package edu.ut.its.controllers;

import edu.ut.its.models.dtos.StreetLogDTO;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.StreetLogService;
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
    public ResponseEntity<ResponseWrapper<Page<StreetLogDTO>>> getAllStreetLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<StreetLogDTO> responses = streetLogService.getAllStreetLogs(pageable);

        ResponseWrapper<Page<StreetLogDTO>> responseWrapper;

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
    public ResponseEntity<ResponseWrapper<StreetLogDTO>> getStreetLog(
            @PathVariable String requestID)
    {
        StreetLogDTO responses = streetLogService.getStreetLogById(requestID);

        ResponseWrapper<StreetLogDTO> responseWrapper;

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
    public ResponseEntity<ResponseWrapper<StreetLogDTO>> createStreetLog(
            @RequestBody StreetLogDTO streetLogDTO)
    {
        try {
            StreetLogDTO responses = streetLogService.createStreetLog(streetLogDTO);

            ResponseWrapper<StreetLogDTO> responseWrapper =
                    new ResponseWrapper<>("Create StreetLog Successfully", responses);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetLogDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<StreetLogDTO>> updateStreetLog(
            @RequestBody StreetLogDTO streetLogDTO,
            @PathVariable String requestID)
    {
        try {
            StreetLogDTO responses = streetLogService.updateStreetLog(requestID, streetLogDTO);

            ResponseWrapper<StreetLogDTO> responseWrapper =
                    new ResponseWrapper<>("Update StreetLog Successfully", responses);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<StreetLogDTO> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
