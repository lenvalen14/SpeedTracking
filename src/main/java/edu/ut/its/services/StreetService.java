package edu.ut.its.services;

import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.mappers.StreetMapper;
import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entities.Street;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.services.impl.IStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StreetService implements IStreetService {

    @Autowired
    private StreetRepo streetRepo;

    @Autowired
    private StreetMapper streetMapper;

    @Override
    public Page<StreetDetailResponse> getAllStreets(Pageable pageable) {
        Page<Street> streets = streetRepo.findAll(pageable);

        if (streets.isEmpty()) {
            throw new DataNotFoundException("Street not found");
        }

        return streets.map(streetMapper::toStreetDTO);
    }

    @Override
    public StreetDetailResponse getStreetById(String id) {
        Street street = streetRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Street not found"));
        return streetMapper.toStreetDTO(street);
    }

    @Override
    public StreetDetailResponse createStreet(StreetCreateRequest streetDTO) {
        if(streetRepo.findByName(streetDTO.getName())) throw new AppException(ErrorCode.STREET_NAME_ALREADY_EXISTS);
        Street street = streetMapper.toStreet(streetDTO);
        return streetMapper.toStreetDTO(streetRepo.save(street));
    }


    @Override
    public StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO) {
        Street existing = streetRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STREET_NOT_FOUND));

        existing.setName(streetDTO.getName());
        existing.setArea(streetDTO.getArea());
        existing.setSpeedLimit(streetDTO.getSpeedLimit());
        existing.setCameraCount(streetDTO.getCameraCount());

        return streetMapper.toStreetDTO(streetRepo.save(existing));
    }
}
