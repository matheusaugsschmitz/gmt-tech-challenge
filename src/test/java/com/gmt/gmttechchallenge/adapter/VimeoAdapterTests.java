package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.client.vimeo.MockedVimeoClient;
import com.gmt.gmttechchallenge.client.vimeo.VimeoVideoDto;
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
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VimeoAdapterTests {

    @InjectMocks
    private VimeoAdapter vimeoAdapter;

    @Mock
    private MockedVimeoClient vimeoClient;

    @Test
    void testImportBatchMetadata_handleEmptyIdList(){
        // arrange
        List<String> ids = new ArrayList<>();

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertTrue(response.isEmpty());
        verifyNoInteractions(vimeoClient);
    }

    @Test
    void testImportBatchMetadata_handleInvalidIds(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("abc");

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertTrue(response.isEmpty());
        verifyNoInteractions(vimeoClient);
    }

    @Test
    void testImportBatchMetadata_handleEmptyClientResponse(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("123");

        when(vimeoClient.fetchVideosByIdList(singletonList(123L))).thenReturn(Collections.emptyList());

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertTrue(response.isEmpty());
        verify(vimeoClient).fetchVideosByIdList(singletonList(123L));
        verifyNoMoreInteractions(vimeoClient);
    }

    @Test
    void testImportBatchMetadata_handleHalfVideosFoundBecauseOfInvalidId(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("abc");

        long sourceVideoId = 321L;
        ids.add(String.valueOf(sourceVideoId));

        LocalDateTime videoUploadTime = LocalDateTime.now();
        String channelName = "channel abc";
        String[] tags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        long viewCount = 32478240L;
        long durationMs = 54165163654L;
        var vimeoVideoDto = new VimeoVideoDto(sourceVideoId, videoUploadTime, durationMs, tags, viewCount, channelName);

        when(vimeoClient.fetchVideosByIdList(singletonList(sourceVideoId))).thenReturn(singletonList(vimeoVideoDto));
        var expectedUUID = UUID.nameUUIDFromBytes("VIMEO:321".getBytes(StandardCharsets.UTF_8));

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertEquals(1, response.size(), "Expected 1 video metadata, but got " + response.size());
        VideoMetadata videoMetadata = response.getFirst();
        assertEquals(expectedUUID, videoMetadata.id(), "Expected id " + sourceVideoId + ", but got " + videoMetadata.id());
        assertEquals(String.valueOf(sourceVideoId), videoMetadata.sourceId(), "Expected source id " + sourceVideoId + ", but got " + videoMetadata.sourceId());
        assertEquals(VideoSource.VIMEO, videoMetadata.source(), "Expected source " + VideoSource.VIMEO + ", but got " + videoMetadata.source());
        assertEquals(videoUploadTime, videoMetadata.publishedAt(), "Expected published at " + videoUploadTime + ", but got " + videoMetadata.publishedAt());
        assertEquals(channelName, videoMetadata.channelTitle(), "Expected channel title " + channelName + ", but got " + videoMetadata.channelTitle());
        assertEquals(tags, videoMetadata.tags(), "Expected tags " + asList(tags) + ", but got " + asList(videoMetadata.tags()));
        assertEquals(BigInteger.valueOf(viewCount), videoMetadata.viewCount(), "Expected view count " + viewCount + ", but got " + videoMetadata.viewCount());
        assertEquals(durationMs, videoMetadata.durationMs(), "Expected duration " + durationMs + ", but got " + videoMetadata.durationMs());

        verify(vimeoClient).fetchVideosByIdList(singletonList(sourceVideoId));
        verifyNoMoreInteractions(vimeoClient);
    }

    @Test
    void testImportBatchMetadata_handleHalfVideosFound(){
        // arrange
        List<String> ids = new ArrayList<>();
        ids.add("123");

        long sourceVideoId = 321L;
        ids.add(String.valueOf(sourceVideoId));

        LocalDateTime videoUploadTime = LocalDateTime.now();
        String channelName = "channel abc";
        String[] tags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        long viewCount = 32478240L;
        long durationMs = 54165163654L;
        var vimeoVideoDto = new VimeoVideoDto(sourceVideoId, videoUploadTime, durationMs, tags, viewCount, channelName);

        when(vimeoClient.fetchVideosByIdList(asList(123L, sourceVideoId))).thenReturn(singletonList(vimeoVideoDto));
        var expectedUUID = UUID.nameUUIDFromBytes("VIMEO:321".getBytes(StandardCharsets.UTF_8));

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertEquals(1, response.size(), "Expected 1 video metadata, but got " + response.size());
        VideoMetadata videoMetadata = response.getFirst();
        assertEquals(expectedUUID, videoMetadata.id(), "Expected id " + sourceVideoId + ", but got " + videoMetadata.id());
        assertEquals(String.valueOf(sourceVideoId), videoMetadata.sourceId(), "Expected source id " + sourceVideoId + ", but got " + videoMetadata.sourceId());
        assertEquals(VideoSource.VIMEO, videoMetadata.source(), "Expected source " + VideoSource.VIMEO + ", but got " + videoMetadata.source());
        assertEquals(videoUploadTime, videoMetadata.publishedAt(), "Expected published at " + videoUploadTime + ", but got " + videoMetadata.publishedAt());
        assertEquals(channelName, videoMetadata.channelTitle(), "Expected channel title " + channelName + ", but got " + videoMetadata.channelTitle());
        assertEquals(tags, videoMetadata.tags(), "Expected tags " + asList(tags) + ", but got " + asList(videoMetadata.tags()));
        assertEquals(BigInteger.valueOf(viewCount), videoMetadata.viewCount(), "Expected view count " + viewCount + ", but got " + videoMetadata.viewCount());
        assertEquals(durationMs, videoMetadata.durationMs(), "Expected duration " + durationMs + ", but got " + videoMetadata.durationMs());

        verify(vimeoClient).fetchVideosByIdList(asList(123L, sourceVideoId));
        verifyNoMoreInteractions(vimeoClient);
    }

    @Test
    void testImportBatchMetadata_handleAllVideosFound(){
        // arrange
        List<String> ids = new ArrayList<>();
        long firstVideoSourceId = 123L;
        String firstVideoSourceIdAsString = String.valueOf(firstVideoSourceId);
        LocalDateTime firstVideoUploadTime = LocalDateTime.now().minusHours(12);
        String firstVideoChannelName = "channel abc";
        String[] firstVideoTags = {"n8n", "k8s", "cluster", "proxmox", "homelab"};
        long firstVideoViewCount = 32478240L;
        long firstVideoDurationMs = 54165163654L;

        long secondVideoSourceId = 321L;
        String secondVideoSourceIdAsString = "321";
        LocalDateTime secondVideoUploadTime = LocalDateTime.now().minusHours(5);
        String secondVideoChannelName = "channel cde";
        String[] secondVideoTags = {"vmware", "azure", "sap"};
        long secondVideoViewCount = 32478240L;
        long secondVideoDurationMs = 54165163654L;

        ids.add(firstVideoSourceIdAsString);
        var firstVimeoVideoDto = new VimeoVideoDto(firstVideoSourceId, firstVideoUploadTime, firstVideoDurationMs, firstVideoTags, firstVideoViewCount, firstVideoChannelName);

        ids.add(secondVideoSourceIdAsString);
        var secondVimeoVideoDto = new VimeoVideoDto(secondVideoSourceId, secondVideoUploadTime, secondVideoDurationMs, secondVideoTags, secondVideoViewCount, secondVideoChannelName);

        when(vimeoClient.fetchVideosByIdList(asList(firstVideoSourceId, secondVideoSourceId))).thenReturn(asList(firstVimeoVideoDto, secondVimeoVideoDto));
        var firstVideoExpectedUUID = UUID.nameUUIDFromBytes("VIMEO:123".getBytes(StandardCharsets.UTF_8));
        var secondVideoExpectedUUID = UUID.nameUUIDFromBytes("VIMEO:321".getBytes(StandardCharsets.UTF_8));

        // act
        List<VideoMetadata> response = vimeoAdapter.importBatchMetadata(ids);

        // assert
        assertEquals(2, response.size(), "Expected 2 videos metadata, but got " + response.size());
        VideoMetadata firstVideoMetadata = response.getFirst();
        assertEquals(firstVideoExpectedUUID, firstVideoMetadata.id(), "Expected id " + firstVideoSourceIdAsString + ", but got " + firstVideoMetadata.id());
        assertEquals(firstVideoSourceIdAsString, firstVideoMetadata.sourceId(), "Expected source id " + firstVideoSourceIdAsString + ", but got " + firstVideoMetadata.sourceId());
        assertEquals(VideoSource.VIMEO, firstVideoMetadata.source(), "Expected source " + VideoSource.VIMEO + ", but got " + firstVideoMetadata.source());
        assertEquals(firstVideoUploadTime, firstVideoMetadata.publishedAt(), "Expected published at " + firstVideoUploadTime + ", but got " + firstVideoMetadata.publishedAt());
        assertEquals(firstVideoChannelName, firstVideoMetadata.channelTitle(), "Expected channel title " + firstVideoChannelName + ", but got " + firstVideoMetadata.channelTitle());
        assertEquals(firstVideoTags, firstVideoMetadata.tags(), "Expected tags " + asList(firstVideoTags) + ", but got " + asList(firstVideoMetadata.tags()));
        assertEquals(BigInteger.valueOf(firstVideoViewCount), firstVideoMetadata.viewCount(), "Expected view count " + firstVideoViewCount + ", but got " + firstVideoMetadata.viewCount());
        assertEquals(firstVideoDurationMs, firstVideoMetadata.durationMs(), "Expected duration " + firstVideoDurationMs + ", but got " + firstVideoMetadata.durationMs());

        VideoMetadata secondVideoMetadata = response.get(1);
        assertEquals(secondVideoExpectedUUID, secondVideoMetadata.id(), "Expected id " + secondVideoSourceIdAsString + ", but got " + secondVideoMetadata.id());
        assertEquals(secondVideoSourceIdAsString, secondVideoMetadata.sourceId(), "Expected source id " + secondVideoSourceIdAsString + ", but got " + secondVideoMetadata.sourceId());
        assertEquals(VideoSource.VIMEO, secondVideoMetadata.source(), "Expected source " + VideoSource.VIMEO + ", but got " + secondVideoMetadata.source());
        assertEquals(secondVideoUploadTime, secondVideoMetadata.publishedAt(), "Expected published at " + secondVideoUploadTime + ", but got " + secondVideoMetadata.publishedAt());
        assertEquals(secondVideoChannelName, secondVideoMetadata.channelTitle(), "Expected channel title " + secondVideoChannelName + ", but got " + secondVideoMetadata.channelTitle());
        assertEquals(secondVideoTags, secondVideoMetadata.tags(), "Expected tags " + asList(secondVideoTags) + ", but got " + asList(secondVideoMetadata.tags()));
        assertEquals(BigInteger.valueOf(secondVideoViewCount), secondVideoMetadata.viewCount(), "Expected view count " + secondVideoViewCount + ", but got " + secondVideoMetadata.viewCount());
        assertEquals(secondVideoDurationMs, secondVideoMetadata.durationMs(), "Expected duration " + secondVideoDurationMs + ", but got " + secondVideoMetadata.durationMs());

        verify(vimeoClient).fetchVideosByIdList(asList(firstVideoSourceId, secondVideoSourceId));
        verifyNoMoreInteractions(vimeoClient);
    }

}
