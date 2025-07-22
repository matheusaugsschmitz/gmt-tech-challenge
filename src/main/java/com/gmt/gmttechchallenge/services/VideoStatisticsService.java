package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.api.VideosStatisticsPerSource;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Service
@RequiredArgsConstructor
public class VideoStatisticsService {

    private final VideoMetadataRepository repository;

    public List<VideosStatisticsPerSource> fetchAllStatistics(){
        List<VideoMetadata> videos = repository.findAll();

        Map<VideoSource, List<VideoMetadata>> videosPerSource = videos.stream()
                .collect(groupingBy(VideoMetadata::source, mapping(Function.identity(), Collectors.toList())));

        List<VideosStatisticsPerSource> statistics = new ArrayList<>();
        videosPerSource.forEach((key, value) -> {
            double averageDuration = value.stream()
                    .mapToLong(VideoMetadata::durationMs)
                    .average()
                    .orElse(0.0);
            statistics.add(new VideosStatisticsPerSource(key, value.size(), averageDuration));
        });

        return statistics;
    }
}
