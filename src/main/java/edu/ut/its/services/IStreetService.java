package edu.ut.its.services;

import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import edu.ut.its.models.entitys.Street;

import java.util.List;
import java.util.Optional;

public interface IStreetService {
    List<StreetDetailResponse> getAllStreets();
    StreetDetailResponse getStreetById(String id);
    StreetDetailResponse createStreet(StreetCreateRequest streetDTO);
    StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO);
}
