package edu.ut.its.services;

import edu.ut.its.models.dtos.StreetDTO;

import java.util.List;
import java.util.Optional;

public interface IStreetService {
    List<StreetDTO> getAllStreets();
    Optional<StreetDTO> getStreetById(String id);
    StreetDTO createStreet(StreetDTO streetDTO);
    StreetDTO updateStreet(String id, StreetDTO streetDTO);
}
