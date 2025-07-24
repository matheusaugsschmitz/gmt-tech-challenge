package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.client.youtube.MockedYouTubeClient;
import com.gmt.gmttechchallenge.client.youtube.YouTubeVideoDto;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class YoutubeAdapterTests {

    @InjectMocks
    private YoutubeAdapter youtubeAdapter;

    @Mock
    private MockedYouTubeClient youTubeProxy;

    @Test
    void testFetchBatchMetadata_handleEmptyIdList(){
        // arrange
        List<String> ids = new ArrayList<>();

        // act
        List<VideoMetadata> response = youtubeAdapter.fetchBatchMetadata(ids);

        // assert
        assertTrue(response.isEmpty());
        verifyNoInteractions(youTubeProxy);
    }

    @Test
    void testFetchBatchMetadata_handleEmptyClientResponse(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("123");

        when(youTubeProxy.fetchVideosByIdList(ids)).thenReturn(Collections.emptyList());

        // act
        List<VideoMetadata> response = youtubeAdapter.fetchBatchMetadata(ids);

        // assert
        assertTrue(response.isEmpty());
        verify(youTubeProxy).fetchVideosByIdList(ids);
        verifyNoMoreInteractions(youTubeProxy);
    }

    @Test
    void testFetchBatchMetadata_handleHalfVideosFound(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("123");

        String sourceVideoId = "321";
        ids.add(sourceVideoId);

        LocalDateTime videoUploadTime = LocalDateTime.now();
        String channelName = "channel abc";
        String[] tags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        var ytSnippet = new YouTubeVideoDto.Snippet(videoUploadTime, channelName, tags);

        String viewCount = "32478240";
        var ytStatistics = new YouTubeVideoDto.Statistics(viewCount);

        long durationMs = 54165163654L;
        var ytFileDetails = new YouTubeVideoDto.FileDetails(durationMs);

        var youtubeVideoDto = new YouTubeVideoDto(sourceVideoId, ytSnippet, ytStatistics, ytFileDetails);

        when(youTubeProxy.fetchVideosByIdList(ids)).thenReturn(Collections.singletonList(youtubeVideoDto));
        var expectedUUID = UUID.nameUUIDFromBytes("YOUTUBE:321".getBytes(StandardCharsets.UTF_8));

        // act
        List<VideoMetadata> response = youtubeAdapter.fetchBatchMetadata(ids);

        // assert
        assertEquals(1, response.size(), "Expected 1 video metadata, but got " + response.size());
        VideoMetadata videoMetadata = response.getFirst();
        assertEquals(expectedUUID, videoMetadata.id(), "Expected id " + sourceVideoId + ", but got " + videoMetadata.id());
        assertEquals(sourceVideoId, videoMetadata.sourceId(), "Expected source id " + sourceVideoId + ", but got " + videoMetadata.sourceId());
        assertEquals(VideoSource.YOUTUBE, videoMetadata.source(), "Expected source " + VideoSource.YOUTUBE + ", but got " + videoMetadata.source());
        assertEquals(videoUploadTime, videoMetadata.publishedAt(), "Expected published at " + videoUploadTime + ", but got " + videoMetadata.publishedAt());
        assertEquals(channelName, videoMetadata.channelTitle(), "Expected channel title " + channelName + ", but got " + videoMetadata.channelTitle());
        assertEquals(tags, videoMetadata.tags(), "Expected tags " + asList(tags) + ", but got " + asList(videoMetadata.tags()));
        assertEquals(new BigInteger(viewCount), videoMetadata.viewCount(), "Expected view count " + viewCount + ", but got " + videoMetadata.viewCount());
        assertEquals(durationMs, videoMetadata.durationMs(), "Expected duration " + durationMs + ", but got " + videoMetadata.durationMs());

        verify(youTubeProxy).fetchVideosByIdList(ids);
        verifyNoMoreInteractions(youTubeProxy);
    }

    @Test
    void testFetchBatchMetadata_handleAllVideosFound(){
        // arrange
        List<String> ids = new ArrayList<>();
        String firstVideoSourceId = "123";
        LocalDateTime firstVideoUploadTime = LocalDateTime.now().minusHours(12);
        String firstVideoChannelName = "channel abc";
        String[] firstVideoTags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        String firstVideoViewCount = "32478240";
        long firstVideoDurationMs = 54165163654L;

        String secondVideoSourceId = "321";
        LocalDateTime secondVideoUploadTime = LocalDateTime.now().minusHours(5);
        String secondVideoChannelName = "channel abc";
        String[] secondVideoTags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        String secondVideoViewCount = "32478240";
        long secondVideoDurationMs = 54165163654L;

        ids.add(firstVideoSourceId);
        var firstYoutubeVideoDto = new YouTubeVideoDto(
                firstVideoSourceId,
                new YouTubeVideoDto.Snippet(firstVideoUploadTime, firstVideoChannelName, firstVideoTags),
                new YouTubeVideoDto.Statistics(firstVideoViewCount),
                new YouTubeVideoDto.FileDetails(firstVideoDurationMs)
        );

        ids.add(secondVideoSourceId);
        var secondYoutubeVideoDto = new YouTubeVideoDto(
                secondVideoSourceId,
                new YouTubeVideoDto.Snippet(secondVideoUploadTime, secondVideoChannelName, secondVideoTags),
                new YouTubeVideoDto.Statistics(secondVideoViewCount),
                new YouTubeVideoDto.FileDetails(secondVideoDurationMs)
        );

        when(youTubeProxy.fetchVideosByIdList(ids)).thenReturn(asList(firstYoutubeVideoDto, secondYoutubeVideoDto));
        var firstVideoExpectedUUID = UUID.nameUUIDFromBytes("YOUTUBE:123".getBytes(StandardCharsets.UTF_8));
        var secondVideoExpectedUUID = UUID.nameUUIDFromBytes("YOUTUBE:321".getBytes(StandardCharsets.UTF_8));

        // act
        List<VideoMetadata> response = youtubeAdapter.fetchBatchMetadata(ids);

        // assert
        assertEquals(2, response.size(), "Expected 2 videos metadata, but got " + response.size());
        VideoMetadata firstVideoMetadata = response.getFirst();
        assertEquals(firstVideoExpectedUUID, firstVideoMetadata.id(), "Expected id " + firstVideoSourceId + ", but got " + firstVideoMetadata.id());
        assertEquals(firstVideoSourceId, firstVideoMetadata.sourceId(), "Expected source id " + firstVideoSourceId + ", but got " + firstVideoMetadata.sourceId());
        assertEquals(VideoSource.YOUTUBE, firstVideoMetadata.source(), "Expected source " + VideoSource.YOUTUBE + ", but got " + firstVideoMetadata.source());
        assertEquals(firstVideoUploadTime, firstVideoMetadata.publishedAt(), "Expected published at " + firstVideoUploadTime + ", but got " + firstVideoMetadata.publishedAt());
        assertEquals(firstVideoChannelName, firstVideoMetadata.channelTitle(), "Expected channel title " + firstVideoChannelName + ", but got " + firstVideoMetadata.channelTitle());
        assertEquals(firstVideoTags, firstVideoMetadata.tags(), "Expected tags " + asList(firstVideoTags) + ", but got " + asList(firstVideoMetadata.tags()));
        assertEquals(new BigInteger(firstVideoViewCount), firstVideoMetadata.viewCount(), "Expected view count " + firstVideoViewCount + ", but got " + firstVideoMetadata.viewCount());
        assertEquals(firstVideoDurationMs, firstVideoMetadata.durationMs(), "Expected duration " + firstVideoDurationMs + ", but got " + firstVideoMetadata.durationMs());

        VideoMetadata secondVideoMetadata = response.get(1);
        assertEquals(secondVideoExpectedUUID, secondVideoMetadata.id(), "Expected id " + secondVideoSourceId + ", but got " + secondVideoMetadata.id());
        assertEquals(secondVideoSourceId, secondVideoMetadata.sourceId(), "Expected source id " + secondVideoSourceId + ", but got " + secondVideoMetadata.sourceId());
        assertEquals(VideoSource.YOUTUBE, secondVideoMetadata.source(), "Expected source " + VideoSource.YOUTUBE + ", but got " + secondVideoMetadata.source());
        assertEquals(secondVideoUploadTime, secondVideoMetadata.publishedAt(), "Expected published at " + secondVideoUploadTime + ", but got " + secondVideoMetadata.publishedAt());
        assertEquals(secondVideoChannelName, secondVideoMetadata.channelTitle(), "Expected channel title " + secondVideoChannelName + ", but got " + secondVideoMetadata.channelTitle());
        assertEquals(secondVideoTags, secondVideoMetadata.tags(), "Expected tags " + asList(secondVideoTags) + ", but got " + asList(secondVideoMetadata.tags()));
        assertEquals(new BigInteger(secondVideoViewCount), secondVideoMetadata.viewCount(), "Expected view count " + secondVideoViewCount + ", but got " + secondVideoMetadata.viewCount());
        assertEquals(secondVideoDurationMs, secondVideoMetadata.durationMs(), "Expected duration " + secondVideoDurationMs + ", but got " + secondVideoMetadata.durationMs());

        verify(youTubeProxy).fetchVideosByIdList(ids);
        verifyNoMoreInteractions(youTubeProxy);
    }

}
