package com.gmt.gmttechchallenge.api.videoimport;

import com.gmt.gmttechchallenge.services.VideoImportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VideoImportApi {

    private final VideoImportService videoImportService;

    @PostMapping("/videos/import")
    void importVideos(@RequestBody @Valid VideosImportRequest videos){
        videoImportService.importVideoMetadata(videos);
    }
}
