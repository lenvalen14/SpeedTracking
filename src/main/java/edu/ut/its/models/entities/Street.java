package edu.ut.its.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "streets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Street {
    @Id
    private String streetId;

    private String name;
    private String area;
    private int speedLimit;
    private int cameraCount;
}