package edu.ut.its.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.ut.its.models.dtos.requests.StreetLogRequest;
import edu.ut.its.models.dtos.responses.StreetLogResponse;

public interface IStreetLogService {
    Page<StreetLogResponse> getAllStreetLogs(Pageable pageable);
    StreetLogResponse getStreetLogById(String id);
    StreetLogResponse createStreetLogFromJson(StreetLogRequest streetLogDTO);
    StreetLogResponse updateStreetLog(String id, StreetLogResponse streetLogDTO);
}

