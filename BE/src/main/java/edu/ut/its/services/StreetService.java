package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.CameraMapper;
import edu.ut.its.mappers.StreetMapper;
import edu.ut.its.mappers.VideoMapper;
import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entities.Camera;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.Video;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.VideoRepo;
import edu.ut.its.services.impl.IStreetService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StreetService implements IStreetService {

    private final StreetRepo streetRepo;
    private final StreetMapper streetMapper;
    private final CameraRepo cameraRepo;
    private final VideoRepo videoRepo;
    private final VideoMapper videoMapper;

    @Override
    public Page<StreetDetailResponse> getAllStreets(Pageable pageable) {
        Page<Street> streets = streetRepo.findAll(pageable);

        if (streets.isEmpty()) {
            throw new AppException(ErrorCode.STREET_NOT_FOUND);
        }

        return streets.map(street -> {
            List<Camera> cameras = cameraRepo.findByStreet_StreetId(street.getStreetId());
            int cameraCount = cameras.size();

            List<CameraDetailResponse> cameraDTOs = cameras.stream().map(camera -> {
                List<Video> videos = videoRepo.findByCamera_CameraId(camera.getCameraId());
                List<VideoDTO> videoDTOs = videos.stream()
                        .map(videoMapper::toVideoDTO)
                        .toList();

                return new CameraDetailResponse(
                        camera.getCameraId(),
                        streetMapper.toStreetDTO(camera.getStreet()),
                        camera.isStatus(),
                        videoDTOs
                );
            }).toList();

            return new StreetDetailResponse(
                    street.getStreetId(),
                    street.getName(),
                    street.getArea(),
                    street.getSpeedLimit(),
                    cameraCount,
                    street.getLatitude(),
                    street.getLongitude(),
                    cameraDTOs
            );
        });
    }
    

    @Override
    public StreetDetailResponse getStreetById(String id) {
        Street street = streetRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));
        return streetMapper.toStreetDTO(street);
    }

    @Override
    public StreetDetailResponse createStreet(StreetCreateRequest streetDTO) {
        if (streetRepo.findByName(streetDTO.getName()) != null) {
            throw new AppException(ErrorCode.STREET_NAME_ALREADY_EXISTS);
        }
        Street street = streetMapper.toStreet(streetDTO);
        return streetMapper.toStreetDTO(streetRepo.save(street));
    }



    @Override
    public StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO) {
        Street existing = streetRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        existing.setName(streetDTO.getName());
        existing.setArea(streetDTO.getArea());
        existing.setSpeedLimit(streetDTO.getSpeedLimit());
        existing.setCameraCount(streetDTO.getCameraCount());

        return streetMapper.toStreetDTO(streetRepo.save(existing));
    }
}
