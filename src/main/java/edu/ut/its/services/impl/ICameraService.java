package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.CameraCreateRequest;
import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICameraService {
    Page<CameraDetailResponse> getAllCameras(Pageable pageable);
    CameraDetailResponse getCameraById(String id);
    CameraDetailResponse createCamera(CameraCreateRequest cameraDTO, MultipartFile videoFile)  throws IOException;
    CameraDetailResponse updateCamera(String id, CameraUpdateRequest cameraDTO);
    Boolean deleteCamera(String id);
}
