package edu.ut.its.services.impl;

import java.time.LocalDate;

public interface IDashboardService {
    int getTotalVehiclesByStreetAndDate(String streetId, LocalDate date);
    int getTotalCamerasByStreet(String streetId);
    double getAverageSpeedByStreetAndDate(String streetId, LocalDate date);
    double getAverageDensityByStreetAndDate(String streetId, LocalDate date, double streetLength);
}
