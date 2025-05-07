package edu.ut.its.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    private String videoId;

    private String videoUrl;

    @DBRef
    private Camera camera;
}
