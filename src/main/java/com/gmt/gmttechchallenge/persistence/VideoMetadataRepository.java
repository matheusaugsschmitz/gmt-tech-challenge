package com.gmt.gmttechchallenge.persistence;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Repository
public class VideoMetadataRepository {

    private final Map<Integer, VideoMetadata> videoMetadataStorageByID = new HashMap<>();

    public VideoMetadata findById(Integer id){
        return videoMetadataStorageByID.get(id);
    }

    public List<VideoMetadata> findAll(){
        return new ArrayList<>(videoMetadataStorageByID.values());
    }

    // TODO: Implement other filters
    public List<VideoMetadata> findBy(VideoMetadataFilter filter){
        return videoMetadataStorageByID.values().stream()
                .filter(videoMetadata -> {
                    if(nonNull(filter.getSource()) && !filter.getSource().equals(videoMetadata.source()))
                        return false;

                    return true;
                }).collect(toList());
    }

    public void saveAll(List<VideoMetadata> videosMetadata) {
        Optional.ofNullable(videosMetadata)
                .ifPresent(list -> list.forEach(this::save));
    }

    public void save(VideoMetadata videoMetadata) {
        videoMetadataStorageByID.put(videoMetadata.id(), videoMetadata);
    }
}
