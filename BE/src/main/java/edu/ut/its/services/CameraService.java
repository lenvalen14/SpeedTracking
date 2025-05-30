package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.CameraMapper;
import edu.ut.its.mappers.StreetMapper;
import edu.ut.its.mappers.VideoMapper;
import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.dtos.requests.CameraCreateRequest;
import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import edu.ut.its.models.entities.Camera;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.Video;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VideoRepo;
import edu.ut.its.services.impl.ICameraService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CameraService implements ICameraService {

    private final CameraRepo cameraRepo;
    private final StreetRepo streetRepo;
    private final VideoRepo videoRepo;
    private final CameraMapper cameraMapper;
    private final VideoMapper videoMapper;
    private final StreetMapper streetMapper;


    @Override
    public Page<CameraDetailResponse> getAllCameras(Pageable pageable) {
        Page<Camera> cameras = cameraRepo.findAllByStatusTrue(pageable);

        if (cameras.isEmpty()) {
            throw new AppException(ErrorCode.CAMERA_NOT_FOUND);
        }

        return cameras.map(camera -> {
            List<Video> videos = videoRepo.findByCamera_CameraId(camera.getCameraId()); // hoặc byListId
            List<VideoDTO> videoDTOs = videos.stream()
                    .map(videoMapper::toVideoDTO)
                    .collect(Collectors.toList());

            return new CameraDetailResponse(
                    camera.getCameraId(),
                    streetMapper.toStreetDTO(camera.getStreet()),
                    camera.isStatus(),
                    videoDTOs
            );
        });
    }

    @Override
    public CameraDetailResponse getCameraById(String id) {
        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAMERA_NOT_FOUND));
        return cameraMapper.toCameraDTO(camera);
    }

    @Override
    public CameraDetailResponse createCamera(CameraCreateRequest cameraDTO) {
        Street street = streetRepo.findById(cameraDTO.getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        Camera camera = new Camera();
        camera.setStreet(street);
        camera.setStatus(true);

        Camera savedCamera = cameraRepo.save(camera);

        updateStreetCameraCount(savedCamera.getStreet());

        return cameraMapper.toCameraDTO(savedCamera);
    }

    @Override
    public CameraDetailResponse updateCamera(String id, CameraUpdateRequest cameraDTO) {
        Camera existingCamera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAMERA_NOT_FOUND));

        Street street = streetRepo.findById(cameraDTO.getStreet().getStreetId())
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        cameraMapper.updateCameraFromRequest(cameraDTO, existingCamera);

        return cameraMapper.toCameraDTO(cameraRepo.save(existingCamera));
    }

    @Override
    public Boolean deleteCamera(String id) {
        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAMERA_NOT_FOUND));

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