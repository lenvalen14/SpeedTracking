package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.VideoMapper;
import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.entities.Camera;
import edu.ut.its.models.entities.Video;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.VideoRepo;
import edu.ut.its.services.impl.IVideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class VideoService implements IVideoService {

    private final VideoMapper videoMapper;
    private final VideoRepo videoRepo;
    private final FileUploadService fileUploadService;
    private final CallAIService callAIService;
    private final CameraRepo cameraRepo;

    @Override
    public Page<VideoDTO> getAllVideo(Pageable pageable) {
        Page<Video> videos = videoRepo.findAll(pageable);

        if (videos.getTotalElements() == 0) {
            throw new AppException(ErrorCode.VIDEO_NOT_FOUND);
        }

        for (Video video : videos.getContent()) {
            VideoDTO videoDTO = videoMapper.toVideoDTO(video);
            System.out.println("Video entity: " +video);
            System.out.println("Video DTO: " +videoDTO);
        }
        return videos.map(videoMapper::toVideoDTO);
    }

    @Override
    public VideoDTO getVideoById(String id) {
        Video video = videoRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

        return videoMapper.toVideoDTO(video);
    }

    @Override
    public VideoDTO createVideo(String cameraId, MultipartFile videoFile) throws IOException {

        Camera camera = cameraRepo.findByCameraIdAndStatusTrue(cameraId).orElseThrow(() ->
                new AppException(ErrorCode.CAMERA_NOT_FOUND));

        System.out.println(camera);
        Video saveVideo = new Video();

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = fileUploadService.uploadVideo(videoFile);

            saveVideo.setVideoUrl(videoUrl);
        }

        saveVideo.setCamera(camera);

        Video savedVideo = videoRepo.save(saveVideo);

        System.out.println(savedVideo);

        callAIService.autoDetectSpeed(savedVideo.getVideoUrl(), savedVideo.getCamera().getStreet().getSpeedLimit(),
                savedVideo.getCamera().getStreet().getStreetId(), savedVideo.getVideoId());

        Video afterProcess = videoRepo.findById(savedVideo.getVideoId()).orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

        return videoMapper.toVideoDTO(afterProcess);
    }

    @Override
    public VideoDTO updateVideo(String id, VideoDTO videoDTO) {
        Video existingVideo = videoRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND));

        existingVideo.setVideoUrl(videoDTO.getVideoUrl());
        existingVideo = videoRepo.save(existingVideo);
        return videoMapper.toVideoDTO(existingVideo);
    }
}
