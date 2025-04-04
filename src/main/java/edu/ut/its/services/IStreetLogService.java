package edu.ut.its.services;

import edu.ut.its.models.dtos.StreetLogDTO;

import java.util.List;
import java.util.Optional;

public interface IStreetLogService {
    List<StreetLogDTO> getAllStreetLogs();
    StreetLogDTO getStreetLogById(String id);
    StreetLogDTO createStreetLog(StreetLogDTO streetLogDTO);
    StreetLogDTO updateStreetLog(String id, StreetLogDTO streetLogDTO);
    void deleteStreetLog(String id);
}

