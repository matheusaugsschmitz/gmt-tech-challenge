package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.api.videoquery.FetchVideosFilterRequest;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoMetadataRepository repository;

    public Optional<VideoMetadata> fetchById(UUID id) {
        return repository.findById(id);
    }

    public Optional<VideoMetadata> fetchBySourceId(VideoSource source, String id) {
        return fetchById(VideoMetadata.generateId(id, source));
    }

    public List<VideoMetadata> fetchWithAdvancedFiltering(FetchVideosFilterRequest request) {
        return repository.findBy(videoMetadata -> {
            if (nonNull(request.source()) && !videoMetadata.source().equals(request.source()))
                return false;

            if (nonNull(request.sourceId()) && !videoMetadata.sourceId().equals(request.sourceId()))
                return false;

            if  (nonNull(request.uploadDate()) && !videoMetadata.publishedAt().toLocalDate().equals(request.uploadDate()))
                return false;

            if (nonNull(request.minDurationMs()) && request.minDurationMs().compareTo(videoMetadata.durationMs()) > 0)
                return false;

            if (nonNull(request.maxDurationMs()) && request.maxDurationMs().compareTo(videoMetadata.durationMs()) < 0)
                return false;

            return true;
        });
    }
}
