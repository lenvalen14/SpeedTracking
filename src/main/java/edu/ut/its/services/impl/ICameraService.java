package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;

import java.util.List;
import java.util.Optional;

public interface ICameraService {
    List<CameraDetailResponse> getAllCameras();
    CameraDetailResponse getCameraById(String id);
    CameraDetailResponse createCamera(CameraDetailResponse cameraDTO);
    CameraDetailResponse updateCamera(String id, CameraUpdateRequest cameraDTO);
    void deleteCamera(String id);
}
