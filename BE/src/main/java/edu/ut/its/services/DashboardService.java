package edu.ut.its.services;

import edu.ut.its.configuration.CacheDB;
import edu.ut.its.models.dtos.responses.ChartDataResponse;
import edu.ut.its.models.dtos.responses.ViolationRate;
import edu.ut.its.models.entities.Street;
import edu.ut.its.models.entities.StreetLog;
import edu.ut.its.repositories.CameraRepo;
import edu.ut.its.repositories.StreetLogRepo;
import edu.ut.its.repositories.StreetRepo;
import edu.ut.its.repositories.ViolationRepo;
import edu.ut.its.services.impl.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ViolationRepo violationRepo;
    private final StreetLogRepo streetLogRepo;
    private final StreetRepo streetRepo;
    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * ?") // chạy lúc 00:00 mỗi ngày
    public void cacheViolationCount() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYesterday = now.toLocalDate().minusDays(1).atStartOfDay();
        LocalDateTime endOfYesterday = startOfYesterday.plusDays(1).minusNanos(1);

        List<Street> streets = streetRepo.findAll();

        for (Street street : streets) {
            int violationCount = violationRepo.countByStreet_StreetIdAndCreateAtBetween(
                    street.getStreetId(), startOfYesterday, endOfYesterday
            );

            // Lưu vào Redis cache
            cacheManager.getCache("violationYesterday").put(street.getStreetId(), violationCount);
        }
    }

    @Override
    public ViolationRate getViolationCountRealtime(String streetId) {
        LocalDateTime from = LocalDate.now().atStartOfDay(); // 00:00 hôm nay
        LocalDateTime to = LocalDateTime.now();

        Integer count = (Integer) cacheManager.getCache("violationYesterday")
                .get(streetId, Integer.class);

        Integer countNow = violationRepo.countByStreet_StreetIdAndCreateAtBetween(streetId, from, to);

        ViolationRate violationRate = new ViolationRate();
        violationRate.setViolationCount(countNow);

        if (countNow > count) {
            violationRate.setViolationRate(countNow/count *100);
            violationRate.setViolationType("Increase");
            violationRate.setViolationCount(countNow);
        }
        else if (countNow < count) {
            violationRate.setViolationRate(count/countNow *100);
            violationRate.setViolationType("Decrease");
        }
        else{
            violationRate.setViolationRate(0);
            violationRate.setViolationType("Same");
        }

        return violationRate;
    }

    @Override
    public ChartDataResponse getRealtimeStats(String streetId) {
        LocalDateTime from = LocalDate.now().atStartOfDay(); // 00:00 hôm nay
        LocalDateTime to = LocalDateTime.now();

        List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(streetId, from, to);

        double avgSpeed = logs.stream().mapToDouble(StreetLog::getSpeedAvg).average().orElse(0);
        double avgDensity = logs.stream().mapToDouble(StreetLog::getDensity).average().orElse(0);
        double avgVehicles = logs.stream().mapToInt(StreetLog::getVehiclesCount).average().orElse(0);

        return new ChartDataResponse(
                List.of(avgSpeed),
                List.of(avgDensity),
                List.of(avgVehicles)
        );
    }

    @Override
    public ChartDataResponse getWeeklyStats(String streetId, int year, int month) {
        List<Double> speedList = new ArrayList<>();
        List<Double> densityList = new ArrayList<>();
        List<Double> trafficList = new ArrayList<>();

        for (int week = 1; week <= 4; week++) {
            LocalDateTime from = LocalDateTime.of(year, month, (week - 1) * 7 + 1, 0, 0, 0, 0);
            LocalDateTime to = LocalDateTime.of(year, month, Math.min(week * 7, from.toLocalDate().lengthOfMonth()), 23, 59, 59, 999999999);

            List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(streetId, from, to);

            if (!logs.isEmpty()) {
                double avgSpeed = logs.stream().mapToDouble(StreetLog::getSpeedAvg).average().orElse(0);
                double avgDensity = logs.stream().mapToDouble(StreetLog::getDensity).average().orElse(0);
                double avgVehicles = logs.stream().mapToInt(StreetLog::getVehiclesCount).average().orElse(0);

                speedList.add(avgSpeed);
                densityList.add(avgDensity);
                trafficList.add(avgVehicles);
            } else {
                speedList.add(0.0);
                densityList.add(0.0);
                trafficList.add(0.0);
            }
        }

        return new ChartDataResponse(speedList, densityList, trafficList);
    }

    @Override
    public ChartDataResponse getMonthlyStats(String streetId, int year) {
        List<Double> speedList = new ArrayList<>();
        List<Double> densityList = new ArrayList<>();
        List<Double> trafficList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
            LocalDateTime to = from.withDayOfMonth(from.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            List<StreetLog> logs = streetLogRepo.findByStreet_StreetIdAndCreateAtBetween(streetId, from, to);

            System.out.println(logs);

            if (!logs.isEmpty()) {
                double avgSpeed = logs.stream().mapToDouble(StreetLog::getSpeedAvg).average().orElse(0);
                double avgDensity = logs.stream().mapToDouble(StreetLog::getDensity).average().orElse(0);
                double avgVehicles = logs.stream().mapToInt(StreetLog::getVehiclesCount).average().orElse(0);

                speedList.add(avgSpeed);
                densityList.add(avgDensity);
                trafficList.add(avgVehicles);
            } else {
                speedList.add(0.0);
                densityList.add(0.0);
                trafficList.add(0.0);
            }
        }

        return new ChartDataResponse(speedList, densityList, trafficList);
    }
}
