package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.StreetLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStreetLogService {
    Page<StreetLogDTO> getAllStreetLogs(Pageable pageable);
    StreetLogDTO getStreetLogById(String id);
    StreetLogDTO createStreetLog(StreetLogDTO streetLogDTO);
    StreetLogDTO updateStreetLog(String id, StreetLogDTO streetLogDTO);
}

