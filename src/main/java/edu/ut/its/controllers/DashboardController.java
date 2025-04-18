package edu.ut.its.controllers;

import edu.ut.its.services.impl.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/vehicles")
    public int getTotalVehicles(
            @RequestParam String streetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dashboardService.getTotalVehiclesByStreetAndDate(streetId, date);
    }

    @GetMapping("/cameras")
    public int getTotalCameras(@RequestParam String streetId) {
        return dashboardService.getTotalCamerasByStreet(streetId);
    }

    @GetMapping("/average-speed")
    public double getAverageSpeed(
            @RequestParam String streetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dashboardService.getAverageSpeedByStreetAndDate(streetId, date);
    }

    @GetMapping("/average-density")
    public double getAverageDensity(
            @RequestParam String streetId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dashboardService.getAverageDensityByStreetAndDate(streetId, date);
    }
}
