package edu.ut.its.services;

import edu.ut.its.models.dtos.CameraDTO;

import java.util.List;
import java.util.Optional;

public interface ICameraService {
    List<CameraDTO> getAllCameras();
    Optional<CameraDTO> getCameraById(String id);
    CameraDTO createCamera(CameraDTO cameraDTO);
    CameraDTO updateCamera(String id, CameraDTO cameraDTO);
    void deleteCamera(String id);
}
