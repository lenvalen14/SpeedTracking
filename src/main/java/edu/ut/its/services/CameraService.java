package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mappers.CameraMapper;
import edu.ut.its.models.dtos.requests.CameraCreateRequest;
import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import edu.ut.its.models.entities.Camera;
import edu.ut.its.models.entities.Street;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.services.impl.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CameraService implements ICameraService {

    @Autowired
    private CameraRepo cameraRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private CameraMapper cameraMapper;


    @Override
    public Page<CameraDetailResponse> getAllCameras(Pageable pageable) {
        Page<Camera> cameras = cameraRepo.findAll(pageable);

        if (cameras.isEmpty()) {
            throw new DataNotFoundException("No cameras found");
        }

        return cameras.map(cameraMapper::toCameraDTO);
    }

    @Override
    public CameraDetailResponse getCameraById(String id) {
        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Camera with ID " + id + " not found"));
        return cameraMapper.toCameraDTO(camera);
    }

    @Override
    public CameraDetailResponse createCamera(CameraCreateRequest cameraDTO) {
        Street street = streetRepo.findById(cameraDTO.getStreetId())
                .orElseThrow(() -> new DataNotFoundException("Street not found with ID: " + cameraDTO.getStreetId()));

        Camera camera = new Camera();
        camera.setStreet(street);
        camera.setStatus(true);

        Camera savedCamera = cameraRepo.save(camera);

        updateStreetCameraCount(street);

        return cameraMapper.toCameraDTO(savedCamera);
    }

    @Override
    public CameraDetailResponse updateCamera(String id, CameraUpdateRequest cameraDTO) {
        Camera existingCamera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Camera with ID " + id + " not found"));

        Street street = streetRepo.findById(cameraDTO.getStreet().getStreetId())
                .orElseThrow(() -> new DataNotFoundException("Street with ID " + cameraDTO.getStreet().getStreetId() + " not found"));

        cameraMapper.updateCameraFromRequest(cameraDTO, existingCamera);

        return cameraMapper.toCameraDTO(cameraRepo.save(existingCamera));
    }

    @Override
    public Boolean deleteCamera(String id) {
        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Camera with ID " + id + " not found"));

        camera.setStatus(false);
        cameraRepo.save(camera);

        updateStreetCameraCount(camera.getStreet());

        return true;
    }

    private void updateStreetCameraCount(Street street) {
        long cameraCount = cameraRepo.countByStreetAndStatusTrue(street);
        street.setCameraCount((int) cameraCount);
        streetRepo.save(street);
    }
}