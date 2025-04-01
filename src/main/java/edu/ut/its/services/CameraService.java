package edu.ut.its.services;

import edu.ut.its.models.dtos.CameraDTO;
import edu.ut.its.models.entitys.Camera;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CameraService implements ICameraService {

    @Autowired
    private CameraRepo cameraRepo;

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private Mapper mapper;

    @Override
    public List<CameraDTO> getAllCameras() {
        return mapper.convertToDtoList(cameraRepo.findAllByStatusTrue(), CameraDTO.class);
    }

    @Override
    public Optional<CameraDTO> getCameraById(String id) {
        return cameraRepo.findByCameraIdAndStatusTrue(id).map(cam -> mapper.convertToDto(cam, CameraDTO.class));
    }

    @Override
    public CameraDTO createCamera(CameraDTO cameraDTO) {

        Street street = streetRepo.findById(cameraDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

        Camera camera = new Camera();
        camera.setStreet(street);
        camera.setStatus(true);

        Camera savedCamera = cameraRepo.save(camera);

        updateStreetCameraCount(street);

        return mapper.convertToDto(savedCamera, CameraDTO.class);
    }

    @Override
    public CameraDTO updateCamera(String id, CameraDTO cameraDTO) {
        Camera existingCamera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("Camera not found"));

        Street street = streetRepo.findById(cameraDTO.getStreet().getStreetId())
                .orElseThrow(() -> new RuntimeException("Street not found"));

        existingCamera.setStreet(street);

        Camera updatedCamera = cameraRepo.save(existingCamera);

        updateStreetCameraCount(street);

        return mapper.convertToDto(updatedCamera, CameraDTO.class);
    }

    @Override
    public void deleteCamera(String id) {
        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("Camera not found"));

        camera.setStatus(false);
        cameraRepo.save(camera);

        updateStreetCameraCount(camera.getStreet());
    }

    private void updateStreetCameraCount(Street street) {
        long cameraCount = cameraRepo.countByStreetAndStatusTrue(street);
        street.setCameraCount((int) cameraCount);
        streetRepo.save(street);
    }
}

