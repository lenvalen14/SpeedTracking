package edu.ut.its.mappers;

import edu.ut.its.models.dtos.ViolationDTO;
import edu.ut.its.models.entities.Violation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ViolationMapper {

    ViolationMapper INSTANCE = Mappers.getMapper(ViolationMapper.class);

    ViolationDTO toViolationDTO(Violation violation);

    Violation toVehicle(ViolationDTO request);

}
