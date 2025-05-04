package edu.ut.its.controllers;

import edu.ut.its.models.dtos.responses.ChartDataResponse;
import edu.ut.its.models.dtos.responses.ViolationRate;
import edu.ut.its.services.impl.IDashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/realtime")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ChartDataResponse getRealtimeStats(
            @RequestParam String streetId
    ) {
        return dashboardService.getRealtimeStats(streetId);
    }


    @GetMapping("/weekly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ChartDataResponse getWeeklyStats(
            @RequestParam String streetId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        ChartDataResponse stats = dashboardService.getWeeklyStats(streetId, year, month);
        System.out.println("Weekly Traffic Stats: " + stats);
        return stats;
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ChartDataResponse getMonthlyStats(
            @RequestParam String streetId,
            @RequestParam int year
    ) {
        ChartDataResponse stats = dashboardService.getMonthlyStats(streetId, year);
        System.out.println("Traffic Stats: " + stats);
        return stats;
    }

    @GetMapping("/violationRate")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ViolationRate getViolationRateStats(
            @RequestParam String steetID
    ){
        ViolationRate response = dashboardService.getViolationCountRealtime(steetID);
        System.out.println("Violation rate: " + response);
        return response;
    }
}
