package com.gmt.gmttechchallenge.api.videostatistics;

import com.gmt.gmttechchallenge.services.VideoStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoStatisticsApi {

    private final VideoStatisticsService service;

    @GetMapping("/videos/stats")
    public List<VideosStatisticsPerSourceResponse> fetchAllStatistics() {
        return service.fetchAllStatistics();
    }
}
