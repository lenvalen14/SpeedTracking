package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.requests.StreetCreateRequest;
import edu.ut.its.models.dtos.responses.StreetDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStreetService {
    Page<StreetDetailResponse> getAllStreets(Pageable pageable);
    StreetDetailResponse getStreetById(String id);
    StreetDetailResponse createStreet(StreetCreateRequest streetDTO);
    StreetDetailResponse updateStreet(String id, StreetDetailResponse streetDTO);
}
