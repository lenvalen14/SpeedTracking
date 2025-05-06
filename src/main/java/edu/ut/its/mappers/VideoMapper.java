package edu.ut.its.mappers;

import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.entities.Video;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CameraMapper.class)
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    VideoDTO toVideoDTO(Video video);

    Video toVideo(VideoDTO videoDTO);

}
