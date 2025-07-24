package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.api.videostatistics.VideosStatisticsPerSourceResponse;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoStatisticsServiceTest {

    @InjectMocks
    VideoStatisticsService videoStatisticsService;

    @Mock
    VideoMetadataRepository videoMetadataRepository;

    @Test
    void testFetchAllStatistics_handleNoVideoMetadata() {
        // arrange
        when(videoMetadataRepository.findAll()).thenReturn(Collections.emptyList());

        // act
        List<VideosStatisticsPerSourceResponse> response = videoStatisticsService.fetchAllStatistics();

        // assert
        assertTrue(response.isEmpty(), "Expected empty response, but got " + response);
    }

    @Test
    void testFetchAllStatistics_handleOnlyYoutubeVideoMetadata() {
        // arrange
        var firstVideo = new VideoMetadata(UUID.randomUUID(), "abc1234", VideoSource.YOUTUBE, LocalDateTime.now(), new String[]{"n8n"}, BigInteger.TEN, "channel abc", 123123123L);
        var secondVideo = new VideoMetadata(UUID.randomUUID(), "abc3545", VideoSource.YOUTUBE, LocalDateTime.now(), new String[]{"homelab"}, BigInteger.TWO, "channel der", 123555123L);
        var thirdVideo = new VideoMetadata(UUID.randomUUID(), "abc6546", VideoSource.YOUTUBE, LocalDateTime.now(), new String[]{"wrecker"}, BigInteger.ONE, "channel ffg", 123777723L);
        when(videoMetadataRepository.findAll()).thenReturn(asList(firstVideo, secondVideo, thirdVideo));

        // act
        List<VideosStatisticsPerSourceResponse> response = videoStatisticsService.fetchAllStatistics();

        // assert
        assertEquals(1, response.size(), "Expected stats for only 1 source, but got " + response);
        VideosStatisticsPerSourceResponse first = response.getFirst();
        assertEquals(3, first.totalVideosImported());
        assertEquals(123485323, first.averageDuration());
    }

    @Test
    void testFetchAllStatistics_handleOnlyVimeoVideoMetadata() {
        // arrange
        var firstVideo = new VideoMetadata(UUID.randomUUID(), "1234", VideoSource.VIMEO, LocalDateTime.now(), new String[]{"n8n"}, BigInteger.TEN, "channel abc", 123146523L);
        var secondVideo = new VideoMetadata(UUID.randomUUID(), "3545", VideoSource.VIMEO, LocalDateTime.now(), new String[]{"homelab"}, BigInteger.TWO, "channel der", 3555123L);
        var thirdVideo = new VideoMetadata(UUID.randomUUID(), "6546", VideoSource.VIMEO, LocalDateTime.now(), new String[]{"wrecker"}, BigInteger.ONE, "channel ffg", 1237723L);
        when(videoMetadataRepository.findAll()).thenReturn(asList(firstVideo, secondVideo, thirdVideo));

        // act
        List<VideosStatisticsPerSourceResponse> response = videoStatisticsService.fetchAllStatistics();

        // assert
        assertEquals(1, response.size(), "Expected stats for only 1 source, but got " + response);
        VideosStatisticsPerSourceResponse first = response.getFirst();
        assertEquals(3, first.totalVideosImported());
        assertEquals(42646456, first.averageDuration());
    }


}
