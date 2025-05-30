package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.dtos.VideoDTOOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface IVideoService {
    Page<VideoDTOOld> getAllVideoNew(Pageable pageable);
    Page<VideoDTO> getAllVideo(Pageable pageable);
    VideoDTO getVideoById(String id);
    VideoDTO createVideo(String cameraId, MultipartFile file) throws Exception;
    VideoDTO updateVideo(String id, VideoDTO videoDTO);
}
