package edu.ut.its.mappers;

import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entities.Street;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StreetMapper {
    StreetMapper INSTANCE = Mappers.getMapper(StreetMapper.class);

    StreetDetailResponse toStreetDTO(Street street);

    @Mapping(target = "streetId", ignore = true)
    Street toStreet(StreetCreateRequest request);
}