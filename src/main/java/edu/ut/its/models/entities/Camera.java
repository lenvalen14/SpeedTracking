package edu.ut.its.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cameras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camera {
    @Id
    private String cameraId;

    @DBRef
    private Street street;

    private boolean status;

    private String videoUrl;
}
