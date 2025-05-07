package edu.ut.its.mappers;

import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.dtos.VideoDTOOld;
import edu.ut.its.models.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = CameraMapper.class)
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    VideoDTO toVideoDTO(Video video);
    VideoDTOOld toVideoDTOOld(Video video);

    Video toVideo(VideoDTO videoDTO);

    List<VideoDTO> toVideoDTO(List<Video> videos);

    List<Video> toVideo(List<VideoDTO> videoDTOs);

}
