package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.services.impl.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final CameraRepo cameraRepo;
    private final StreetLogRepo streetLogRepo;

    private LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime getEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    @Override
    public int getTotalVehiclesByStreetAndDate(String streetId, LocalDate date) {
        List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(
                streetId, getStartOfDay(date), getEndOfDay(date));
        if (logs.isEmpty()) throw new DataNotFoundException("No street logs found for selected date.");

        // Sử dụng sum() để tính tổng số phương tiện
        return logs.stream()
                .mapToInt(StreetLog::getVehiclesCount)
                .sum();
    }

    @Override
    public int getTotalCamerasByStreet(String streetId) {
        return cameraRepo.findByStreet_StreetIdAndStatusTrue(streetId).size();
    }

    @Override
    public double getAverageSpeedByStreetAndDate(String streetId, LocalDate date) {
        List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(
                streetId, getStartOfDay(date), getEndOfDay(date));
        if (logs.isEmpty()) throw new DataNotFoundException("No speed data found for selected date.");
        return logs.stream().mapToDouble(StreetLog::getSpeedAvg).average().orElse(0);
    }

    @Override
    public double getAverageDensityByStreetAndDate(String streetId, LocalDate date) {

        List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(
                streetId, getStartOfDay(date), getEndOfDay(date));
        if (logs.isEmpty()) throw new DataNotFoundException("Không có dữ liệu mật độ cho ngày đã chọn.");

        double averagegetDensity = logs.stream()
                .mapToDouble(StreetLog::getDensity)
                .average()
                .orElse(0);

        return averagegetDensity;
    }

}
