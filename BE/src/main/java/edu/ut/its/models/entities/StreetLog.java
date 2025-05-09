package edu.ut.its.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "street_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetLog {
    @Id
    private String streetLogId;

    @DBRef
    private Street street;

    @DBRef
    private List<Vehicle> vehicles;

    private float speedAvg;
    private double density;
    private int vehiclesCount;
    private int violatorsCount;
    private LocalDateTime createAt;
}
