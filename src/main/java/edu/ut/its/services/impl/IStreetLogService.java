package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.StreetLogDTO;

import java.util.List;

public interface IStreetLogService {
    List<StreetLogDTO> getAllStreetLogs();
    StreetLogDTO getStreetLogById(String id);
    StreetLogDTO createStreetLog(StreetLogDTO streetLogDTO);
    StreetLogDTO updateStreetLog(String id, StreetLogDTO streetLogDTO);
    void deleteStreetLog(String id);
}

