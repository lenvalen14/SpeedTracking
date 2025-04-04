package edu.ut.its.mapper;

import edu.ut.its.models.dtos.StreetLogDTO;
import edu.ut.its.models.entitys.StreetLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StreetLogMapper {
    StreetLogMapper INSTANCE = Mappers.getMapper(StreetLogMapper.class);

    StreetLogDTO toStreetLogDTO(StreetLog streetLog);

    StreetLog toStreetLog(StreetLogDTO request);
}
