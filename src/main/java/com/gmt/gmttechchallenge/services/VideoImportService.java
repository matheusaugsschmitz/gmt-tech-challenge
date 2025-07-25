package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.api.videoimport.VideosImportRequest;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.exception.ResourceNotFoundException;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import com.gmt.gmttechchallenge.adapter.VideoSourceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class VideoImportService {

    private final VideoMetadataRepository videoMetadataRepository;

    private final List<VideoSourceInterface> videoSourceInterfaces;

    public void importVideoMetadata(VideosImportRequest videos) {
        Map<VideoSource, List<String>> videosIdPerSource = videos.videoIdentifiers().stream()
                .collect(groupingBy(VideosImportRequest.VideoIdentifier::source, mapping(VideosImportRequest.VideoIdentifier::videoId, Collectors.toList())));

        videosIdPerSource.forEach(this::asyncImportVideo);
    }

    @Async
    protected void asyncImportVideo(VideoSource videoSource, List<String> ids){
        VideoSourceInterface videoSourceAdapter = videoSourceInterfaces.stream()
                .filter(videoSourceInterface -> videoSourceInterface.getVideoSource().equals(videoSource))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Video source not found: " + videoSource));

        List<VideoMetadata> videosMetadata = videoSourceAdapter.fetchBatchMetadata(ids);
        videoMetadataRepository.saveAll(videosMetadata);
    }
}