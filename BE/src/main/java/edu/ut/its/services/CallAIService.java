package edu.ut.its.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ut.its.exceptions.AppException;
import edu.ut.its.exceptions.ErrorCode;
import edu.ut.its.models.dtos.requests.StreetLogRequest;
import edu.ut.its.models.dtos.responses.StreetLogResponse;
import edu.ut.its.models.entities.Video;
import edu.ut.its.repositories.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CallAIService {

    private final RestTemplate restTemplate;
    private final StreetLogService streetLogService;
    private final VideoRepo videoRepo;

    public String detectSpeed(String videoUrl, int speedLimit) {
        String url = "http://127.0.0.1:8000/detect-speed-from-url";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video_url", videoUrl);
        body.add("speed_limit", String.valueOf(speedLimit));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response.getBody();
    }

    public void autoDetectSpeed(String videoUrl, int speedLimit, String streetID, String videoID) {
        try {
            String jsonResult = detectSpeed(videoUrl, speedLimit);

            System.out.println("AI result: " + jsonResult);

            // Dùng ObjectMapper để chuyển JSON thành object
            ObjectMapper objectMapper = new ObjectMapper();

            // Tạo lớp chứa kết quả từ AI
            StreetLogRequest aiResult = objectMapper.readValue(jsonResult, StreetLogRequest.class);

            StreetLogRequest streetLog = StreetLogRequest.builder()
                    .streetId(streetID)
                    .violators(aiResult.getViolators())
                    .vehicles(aiResult.getVehicles())
                    .speedAvg(aiResult.getSpeedAvg())
                    .vehiclesCount(aiResult.getVehiclesCount())
                    .violatorsCount(aiResult.getViolatorsCount())
                    .output_video_url(aiResult.getOutput_video_url())
                    .build();

            System.out.println("StreetLogRequest: " + streetLog);

            StreetLogResponse response = streetLogService.createStreetLogFromJson(streetLog);

            Video video = videoRepo.findById(videoID).orElseThrow(() ->
                    new AppException(ErrorCode.VIDEO_NOT_FOUND));

            video.setVideoUrl(streetLog.getOutput_video_url());

            videoRepo.save(video);

            System.out.println("response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
