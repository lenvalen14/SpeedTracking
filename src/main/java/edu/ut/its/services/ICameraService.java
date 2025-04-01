package edu.ut.its.services;

import edu.ut.its.models.dtos.responses.CameraDetailResponse;

import java.util.List;
import java.util.Optional;

public interface ICameraService {
    List<CameraDetailResponse> getAllCameras();
    Optional<CameraDetailResponse> getCameraById(String id);
    CameraDetailResponse createCamera(CameraDetailResponse cameraDTO);
    CameraDetailResponse updateCamera(String id, CameraDetailResponse cameraDTO);
    void deleteCamera(String id);
}
