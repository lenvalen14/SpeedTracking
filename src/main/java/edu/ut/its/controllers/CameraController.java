package edu.ut.its.controllers;

import edu.ut.its.models.dtos.requests.CameraCreateRequest;
import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/camera")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @GetMapping()
    public ResponseEntity<ResponseWrapper<Page<CameraDetailResponse>>> getAllCameras(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<CameraDetailResponse> responses = cameraService.getAllCameras(pageable);

        ResponseWrapper<Page<CameraDetailResponse>> responseWrapper;

        if(responses != null && !responses.isEmpty())
        {
            responseWrapper = new ResponseWrapper<>("Data Camera Successfully", responses);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else
        {
            responseWrapper = new ResponseWrapper<>("List Data Of Camera Is Empty", null);
            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<CameraDetailResponse>> getCamera(
            @PathVariable String requestID)
    {
        CameraDetailResponse cameraDetailResponse = cameraService.getCameraById(requestID);

        ResponseWrapper<CameraDetailResponse> responseWrapper;

        if (cameraDetailResponse != null){
            responseWrapper = new ResponseWrapper<>("Data Camera Successfully", cameraDetailResponse);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else{
            responseWrapper = new ResponseWrapper<>("No Data Camera Found", null);

            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<ResponseWrapper<CameraDetailResponse>> createCamera(
            @RequestBody CameraCreateRequest cameraCreateRequest)
    {
        try {
            CameraDetailResponse cameraDetailResponse = cameraService.createCamera(cameraCreateRequest);

            ResponseWrapper<CameraDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Create Camera Successfully", cameraDetailResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<CameraDetailResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @PutMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<CameraDetailResponse>> updateCamera(
            @RequestBody CameraUpdateRequest cameraUpdateRequest,
            @PathVariable String requestID)
    {
        try {
            CameraDetailResponse cameraDetailResponse = cameraService.updateCamera(requestID, cameraUpdateRequest);

            ResponseWrapper<CameraDetailResponse> responseWrapper =
                    new ResponseWrapper<>("Update Camera Successfully", cameraDetailResponse);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<CameraDetailResponse> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }

    @DeleteMapping("/{requestID}")
    public ResponseEntity<ResponseWrapper<Boolean>> deleteCamera(
            @PathVariable String requestID)
    {
        try {
            Boolean deleted = cameraService.deleteCamera(requestID);

            ResponseWrapper<Boolean> responseWrapper =
                    new ResponseWrapper<>("Delete Camera Successfully", deleted);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<Boolean> responseWrapper =
                    new ResponseWrapper<>(e.getMessage(), null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
