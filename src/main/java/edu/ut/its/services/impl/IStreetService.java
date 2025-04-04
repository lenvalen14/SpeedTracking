package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;

import java.util.List;

public interface IStreetService {
    List<StreetDetailResponse> getAllStreets();
    StreetDetailResponse getStreetById(String id);
    StreetDetailResponse createStreet(StreetCreateRequest streetDTO);
    StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO);
}
