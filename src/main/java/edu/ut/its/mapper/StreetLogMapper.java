package edu.ut.its.mapper;

import edu.ut.its.models.dtos.StreetLogDTO;
import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.models.entitys.StreetLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StreetLogMapper {
    StreetLogMapper INSTANCE = Mappers.getMapper(StreetLogMapper.class);

    @Mapping(target = "street.id", source = "streetId")
    StreetLogDTO toStreetLogDTO(StreetLog streetLog);

    StreetLog toStreetLog(StreetLogDTO request);
}
