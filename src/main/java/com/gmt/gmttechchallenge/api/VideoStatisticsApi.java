package com.gmt.gmttechchallenge.api;

import com.gmt.gmttechchallenge.services.VideoStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoStatisticsApi {

    private final VideoStatisticsService service;

    // TODO handle 404
    @GetMapping("/videos/stats")
    public List<VideosStatisticsPerSource> fetchAllStatistics() {
        return service.fetchAllStatistics();
    }
}
