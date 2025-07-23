package com.gmt.gmttechchallenge.api.videoquery;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.services.VideoQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/videos/{id}")
    public ResponseEntity<VideoMetadata> fetchById(@PathVariable UUID id) {
        return queryService.fetchById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/videos/{source}/{id}")
    public ResponseEntity<VideoMetadata> fetchBySourceId(@PathVariable VideoSource source, @PathVariable String id) {
        return queryService.fetchBySourceId(source, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/videos")
    public List<VideoMetadata> fetchByFilter(@ModelAttribute FetchVideosFilterRequest request) {
        return queryService.fetchWithAdvancedFiltering(request);
    }
}
