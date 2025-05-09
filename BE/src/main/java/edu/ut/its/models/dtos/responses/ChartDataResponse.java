package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChartDataResponse {

    private List<Double> speedAverage;
    private List<Double> densityAverage;
    private List<Double> trafficAverage;
}