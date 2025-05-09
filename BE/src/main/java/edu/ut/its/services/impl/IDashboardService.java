package edu.ut.its.services.impl;

import edu.ut.its.models.dtos.responses.ChartDataResponse;
import edu.ut.its.models.dtos.responses.ViolationRate;

import java.time.LocalDate;

public interface IDashboardService {
      ChartDataResponse getRealtimeStats(String streetId);
      ChartDataResponse getWeeklyStats(String streetId, int year, int month);
      ChartDataResponse getMonthlyStats(String streetId, int year);
      ViolationRate getViolationCountRealtime(String streetId);
}
