package com.gmt.gmttechchallenge.api.videoquery;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.services.VideoQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VideoQueryApi {

    private final VideoQueryService queryService;

    // TODO handle 404
    // TODO validate positive number
    @GetMapping("/videos/{id}")
    public VideoMetadata fetchById(@PathVariable UUID id) {
        return queryService.fetchById(id);
    }

    // TODO handle 404
    @GetMapping("/videos/{source}/{id}")
    public VideoMetadata fetchBySourceId(@PathVariable VideoSource source, @PathVariable String id) {
        return queryService.fetchBySourceId(source, id);
    }

    @GetMapping("/videos")
    public List<VideoMetadata> fetchByFilter(@ModelAttribute FetchVideosFilterRequest request) {
        return queryService.fetchWithAdvancedFiltering(request);
    }
}
