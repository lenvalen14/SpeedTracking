package goldiounes.com.vn.its.services;

import goldiounes.com.vn.its.models.dtos.StreetLogDTO;
import java.util.List;
import java.util.Optional;

public interface IStreetLogService {
    List<StreetLogDTO> getAllStreetLogs();
    Optional<StreetLogDTO> getStreetLogById(String id);
    StreetLogDTO createStreetLog(StreetLogDTO streetLogDTO);
    StreetLogDTO updateStreetLog(String id, StreetLogDTO streetLogDTO);
    void deleteStreetLog(String id);
}

