package com.gmt.gmttechchallenge.persistence;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Repository
public class VideoMetadataRepository {

    private final Map<String, VideoMetadata> videoMetadataStorageByID = new HashMap<>();

    public VideoMetadata findById(String id){
        return videoMetadataStorageByID.get(id);
    }

    // TODO: check cast
    public List<VideoMetadata> findAll(){
        return (List<VideoMetadata>) videoMetadataStorageByID.values();
    }

    // TODO: Implement other filters
    public List<VideoMetadata> findBy(VideoMetadataFilter filter){
        return videoMetadataStorageByID.values().stream()
                .filter(videoMetadata -> {
                    if(nonNull(filter.getSource()) && !filter.getSource().equals(videoMetadata.getSource()))
                        return false;

                    return true;
                }).collect(toList());
    }
}
