package edu.ut.its.services;

import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class CallAIService {

    private final RestTemplate restTemplate;

    public String detectSpeed(File videoFile, int speedLimit) throws IOException {
        String url = "http://127.0.0.1:8000/detect-speed";

        // Tạo HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Tạo MultiValueMap chứa form-data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video_file", new FileSystemResource(videoFile));
        body.add("speed_limit", String.valueOf(speedLimit));

        // Tạo HttpEntity chứa headers + body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Gửi POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

//    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void autoDetectSpeed() throws IOException {
        File video = new File("src/main/resources/static/videos/vehicles.mp4");
        String result = detectSpeed(video, 120);
        System.out.println("AI result: " + result);
    }
}
