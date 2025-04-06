package edu.ut.its.mappers;

import edu.ut.its.models.dtos.VehicleDTO;
import edu.ut.its.models.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    VehicleDTO toVehicleDTO(Vehicle vehicle);

    Vehicle toVehicle(VehicleDTO request);
}
