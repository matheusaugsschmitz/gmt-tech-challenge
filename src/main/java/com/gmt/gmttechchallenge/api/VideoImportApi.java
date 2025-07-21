package com.gmt.gmttechchallenge.api;

import com.gmt.gmttechchallenge.services.VideoImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/videos/import")
@RequiredArgsConstructor
public class VideoImportApi {

    private final VideoImportService videoImportService;

    // TODO add spring validation
    @PostMapping
    void importVideos(VideosImport videos){
        videoImportService.importVideoMetadata(videos);
    }
}
