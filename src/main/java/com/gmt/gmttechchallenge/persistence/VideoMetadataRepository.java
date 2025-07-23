package com.gmt.gmttechchallenge.persistence;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Repository
public class VideoMetadataRepository {

    private final Map<UUID, VideoMetadata> videoMetadataStorageByID = new HashMap<>();

    public VideoMetadata findById(UUID id){
        return videoMetadataStorageByID.get(id);
    }

    public List<VideoMetadata> findAll(){
        return new ArrayList<>(videoMetadataStorageByID.values());
    }

    public List<VideoMetadata> findBy(Predicate<VideoMetadata> filters){
        return videoMetadataStorageByID.values().stream()
                .filter(filters)
                .collect(toList());
    }

    public void saveAll(List<VideoMetadata> videosMetadata) {
        Optional.ofNullable(videosMetadata)
                .ifPresent(list -> list.forEach(this::save));
    }

    public void save(VideoMetadata videoMetadata) {
        videoMetadataStorageByID.put(videoMetadata.id(), videoMetadata);
    }
}
