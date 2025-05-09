package edu.ut.its.mappers;

import edu.ut.its.models.dtos.requests.CameraCreateRequest;
import edu.ut.its.models.dtos.requests.CameraUpdateRequest;
import edu.ut.its.models.dtos.responses.CameraDetailResponse;
import edu.ut.its.models.entities.Camera;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CameraMapper {
    CameraMapper INSTANCE = Mappers.getMapper(CameraMapper.class);

    CameraDetailResponse toCameraDTO(Camera camera);

    void updateCameraFromRequest(CameraUpdateRequest request, @MappingTarget Camera camera);

    Camera toCamera(CameraCreateRequest request);
}
