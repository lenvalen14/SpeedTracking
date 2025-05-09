package edu.ut.its.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ut.its.mappers.VideoMapper;
import edu.ut.its.models.dtos.VideoDTO;
import edu.ut.its.models.dtos.VideoDTOOld;
import edu.ut.its.response.ResponseWrapper;
import edu.ut.its.services.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/video")
@AllArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final ObjectMapper objectMapper;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<ResponseWrapper<Page<VideoDTOOld>>> getAllVideo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoDTOOld> responses = videoService.getAllVideoNew(pageable);

        ResponseWrapper<Page<VideoDTOOld>> responseWrapper;

        if (responses != null && !responses.isEmpty()) {
            responseWrapper = new ResponseWrapper<>("Data Video Successfully",responses);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            responseWrapper = new ResponseWrapper<>("No Data Video Found",null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @GetMapping("/{requestID}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<ResponseWrapper<VideoDTO>> getVideo(
            @PathVariable String requestID)
    {
        VideoDTO videoDTO = videoService.getVideoById(requestID);

        ResponseWrapper<VideoDTO> responseWrapper;

        if (videoDTO != null) {
            responseWrapper = new ResponseWrapper<>("Data Video Successfully",videoDTO);

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
        else {
            responseWrapper = new ResponseWrapper<>("No Data Video Found",null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ResponseEntity<ResponseWrapper<VideoDTO>> createVideo(
            @RequestParam("cameraID") String cameraID,
            @RequestParam MultipartFile videoFile)
    {
        try {
            VideoDTO responseDTO = videoService.createVideo(cameraID, videoFile);

            ResponseWrapper<VideoDTO> responseWrapper =
                    new ResponseWrapper<>("Data Video Successfully",responseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        }
        catch (Exception e) {
            ResponseWrapper<VideoDTO> responseWrapper =
                    new ResponseWrapper<>("Create Video Failed",null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        }
    }
}
