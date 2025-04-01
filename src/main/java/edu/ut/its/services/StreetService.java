package edu.ut.its.services;

import edu.ut.its.models.dtos.StreetDTO;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.repositories.StreetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StreetService implements IStreetService {

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private Mapper mapper;

    @Override
    public List<StreetDTO> getAllStreets() {
        return mapper.convertToDtoList(streetRepo.findAll(), StreetDTO.class);
    }

    @Override
    public Optional<StreetDTO> getStreetById(String id) {
        return streetRepo.findById(id)
                .map(st -> mapper.convertToDto(st, StreetDTO.class));
    }

    @Override
    public StreetDTO createStreet(StreetDTO streetDTO) {
        Street street = mapper.convertToEntity(streetDTO, Street.class);
        return mapper.convertToDto(streetRepo.save(street), StreetDTO.class);
    }

    @Override
    public StreetDTO updateStreet(String id, StreetDTO streetDTO) {
        Street existing = streetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Street not found"));

        existing.setName(streetDTO.getName());
        existing.setArea(streetDTO.getArea());
        existing.setSpeedLimit(streetDTO.getSpeedLimit());
        existing.setCameraCount(streetDTO.getCameraCount());

        return mapper.convertToDto(streetRepo.save(existing), StreetDTO.class);
    }
}
