package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoMetadataRepository repository;

    public VideoMetadata fetchById(int id) {
        return repository.findById(id);
    }

    public VideoMetadata fetchBySourceId(VideoSource source, String id) {
        return fetchById(VideoMetadata.generateId(id, source));
    }
}
