package edu.ut.its.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViolationRate {
    private int violationCount;
    private double violationRate;
    private String violationType;
}
