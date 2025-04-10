package edu.ut.its.services;

import edu.ut.its.exceptions.DataNotFoundException;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.models.entities.Vehicle;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.VehicleRepo;
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
        return (int) logs.stream()
                .map(StreetLog::getVehicle)
                .map(Vehicle::getVehicleId)
                .distinct()
                .count();
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
        return logs.stream().mapToDouble(StreetLog::getSpeedAverage).average().orElse(0);
    }

    @Override
    public double getAverageDensityByStreetAndDate(String streetId, LocalDate date, double streetLength) {
        if (streetLength <= 0) throw new IllegalArgumentException("Chiều dài đoạn đường phải lớn hơn 0");

        List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(
                streetId, getStartOfDay(date), getEndOfDay(date));
        if (logs.isEmpty()) throw new DataNotFoundException("Không có dữ liệu mật độ cho ngày đã chọn.");

        double averageVehicleCount = logs.stream()
                .mapToInt(StreetLog::getDensity)
                .average()
                .orElse(0);

        return averageVehicleCount / streetLength; // mật độ xe/km
    }

}
