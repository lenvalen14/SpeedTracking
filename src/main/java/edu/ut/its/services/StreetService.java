package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.mapper.StreetMapper;
import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entitys.Street;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.services.impl.IStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreetService implements IStreetService {

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private StreetMapper streetMapper;

    @Override
    public List<StreetDetailResponse> getAllStreets() {
        List<StreetDetailResponse> streetListDTO = streetRepo.findAll()
                .stream()
                .map(streetMapper::toStreetDTO)
                .toList();
        if (streetListDTO.isEmpty()) {
            throw new DataNotFoundException("No streets found");
        }
        return streetListDTO;
    }

    @Override
    public StreetDetailResponse getStreetById(String id) {
        Street street = streetRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Street not found"));
        return streetMapper.toStreetDTO(street);
    }

    @Override
    public StreetDetailResponse createStreet(StreetCreateRequest streetDTO) {
        if(streetRepo.findByName(streetDTO.getName())) throw new DataNotFoundException("Street name already exists");
        Street street = streetMapper.toStreet(streetDTO);
        return streetMapper.toStreetDTO(streetRepo.save(street));
    }


    @Override
    public StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO) {
        Street existing = streetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Street not found"));

        existing.setName(streetDTO.getName());
        existing.setArea(streetDTO.getArea());
        existing.setSpeedLimit(streetDTO.getSpeedLimit());
        existing.setCameraCount(streetDTO.getCameraCount());

        return streetMapper.toStreetDTO(streetRepo.save(existing));
    }
}
