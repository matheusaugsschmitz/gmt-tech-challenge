package com.gmt.gmttechchallenge.services;

import com.gmt.gmttechchallenge.adapter.VimeoAdapter;
import com.gmt.gmttechchallenge.adapter.YoutubeAdapter;
import com.gmt.gmttechchallenge.api.videoimport.VideosImportRequest;
import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.exception.ResourceNotFoundException;
import com.gmt.gmttechchallenge.persistence.VideoMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoImportServiceTest {

    VideoImportService videoImportService;

    @Mock
    YoutubeAdapter youtubeAdapter;

    @Mock
    VimeoAdapter vimeoAdapter;

    @Mock
    VideoMetadataRepository videoMetadataRepository;

    @BeforeEach
    void setUp(){
        videoImportService = new VideoImportService(videoMetadataRepository, asList(youtubeAdapter, vimeoAdapter));
        lenient().when(youtubeAdapter.getVideoSource()).thenReturn(VideoSource.YOUTUBE);
        lenient().when(vimeoAdapter.getVideoSource()).thenReturn(VideoSource.VIMEO);
    }

    @Test
    void testImportVideos_handleEmptyIdList(){
        // arrange
        List<VideosImportRequest.VideoIdentifier> videoIdentifiers = new ArrayList<>();
        VideosImportRequest videos = new VideosImportRequest(videoIdentifiers);

        // act
        videoImportService.importVideoMetadata(videos);

        // assert
        verifyNoInteractions(youtubeAdapter);
        verifyNoInteractions(vimeoAdapter);
        verifyNoInteractions(videoMetadataRepository);
    }

    @Test
    void testImportVideos_handleNotSupportedVideoSource(){
        // arrange
        List<VideosImportRequest.VideoIdentifier> videoIdentifiers = new ArrayList<>();
        videoIdentifiers.add(new VideosImportRequest.VideoIdentifier("abc123", VideoSource.VIMEO));
        VideosImportRequest videos = new VideosImportRequest(videoIdentifiers);

        // mocking the VimeoAdapter to return a different VideoSource
        when(vimeoAdapter.getVideoSource()).thenReturn(VideoSource.YOUTUBE);

        // act & assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoImportService.importVideoMetadata(videos)
        );

        // extra assert
        assertEquals("Video source not found: VIMEO", exception.getMessage());
        verify(vimeoAdapter).getVideoSource();
        verify(youtubeAdapter).getVideoSource();
        verifyNoMoreInteractions(youtubeAdapter);
        verifyNoMoreInteractions(vimeoAdapter);
        verifyNoInteractions(videoMetadataRepository);
    }

    @Test
    void testImportVideos_handleOnlyVimeoImport(){
        // arrange
        List<VideosImportRequest.VideoIdentifier> videoIdentifiers = new ArrayList<>();
        videoIdentifiers.add(new VideosImportRequest.VideoIdentifier("123456", VideoSource.VIMEO));
        VideosImportRequest videos = new VideosImportRequest(videoIdentifiers);
        List<VideoMetadata> videoMetadataList = new ArrayList<>();
        var vimeoVideoMetadata = new VideoMetadata(UUID.randomUUID(), "64513651", VideoSource.VIMEO, LocalDateTime.now(), new String[]{"k8s"}, BigInteger.TWO, "channel homelab", 123155523L);
        videoMetadataList.add(vimeoVideoMetadata);

        when(vimeoAdapter.fetchBatchMetadata(Collections.singletonList("123456"))).thenReturn(videoMetadataList);

        // act
        videoImportService.importVideoMetadata(videos);

        // assert
        verify(vimeoAdapter).getVideoSource();
        verify(youtubeAdapter, atMostOnce()).getVideoSource();
        verify(videoMetadataRepository).saveAll(videoMetadataList);
        verifyNoMoreInteractions(vimeoAdapter);
        verifyNoMoreInteractions(videoMetadataRepository);
        verifyNoMoreInteractions(youtubeAdapter);
    }

    @Test
    void testImportVideos_handleOnlyYoutubeImport(){
        // arrange
        List<VideosImportRequest.VideoIdentifier> videoIdentifiers = new ArrayList<>();
        videoIdentifiers.add(new VideosImportRequest.VideoIdentifier("123456", VideoSource.YOUTUBE));
        VideosImportRequest videos = new VideosImportRequest(videoIdentifiers);
        List<VideoMetadata> videoMetadataList = new ArrayList<>();
        var youtubeVideoMetadata = new VideoMetadata(UUID.randomUUID(), "abc123", VideoSource.YOUTUBE, LocalDateTime.now(), new String[]{"n8n"}, BigInteger.TEN, "channel abc", 123123123L);
        videoMetadataList.add(youtubeVideoMetadata);

        when(youtubeAdapter.fetchBatchMetadata(Collections.singletonList("123456"))).thenReturn(videoMetadataList);

        // act
        videoImportService.importVideoMetadata(videos);

        // assert
        verify(youtubeAdapter).getVideoSource();
        verify(videoMetadataRepository).saveAll(videoMetadataList);
        verifyNoInteractions(vimeoAdapter);
        verifyNoMoreInteractions(videoMetadataRepository);
        verifyNoMoreInteractions(youtubeAdapter);
    }

    @Test
    void testImportVideos_handleImportForBothSources(){
        // arrange
        List<VideosImportRequest.VideoIdentifier> videoIdentifiers = new ArrayList<>();
        videoIdentifiers.add(new VideosImportRequest.VideoIdentifier("123456", VideoSource.YOUTUBE));
        videoIdentifiers.add(new VideosImportRequest.VideoIdentifier("789451", VideoSource.VIMEO));
        var videos = new VideosImportRequest(videoIdentifiers);
        var youtubeVideoMetadataList = new ArrayList<VideoMetadata>();
        youtubeVideoMetadataList.add(new VideoMetadata(UUID.randomUUID(), "abc123", VideoSource.YOUTUBE, LocalDateTime.now(), new String[]{"n8n"}, BigInteger.TEN, "channel abc", 123123123L));

        var vimeoVideoMetadataList = new ArrayList<VideoMetadata>();
        vimeoVideoMetadataList.add(new VideoMetadata(UUID.randomUUID(), "64513651", VideoSource.VIMEO, LocalDateTime.now(), new String[]{"k8s"}, BigInteger.TWO, "channel homelab", 123155523L));


        when(youtubeAdapter.fetchBatchMetadata(Collections.singletonList("123456"))).thenReturn(youtubeVideoMetadataList);
        when(vimeoAdapter.fetchBatchMetadata(Collections.singletonList("789451"))).thenReturn(vimeoVideoMetadataList);

        // act
        videoImportService.importVideoMetadata(videos);

        // assert
        verify(youtubeAdapter, times(2)).getVideoSource();
        verify(vimeoAdapter).getVideoSource();
        verify(videoMetadataRepository).saveAll(youtubeVideoMetadataList);
        verify(videoMetadataRepository).saveAll(vimeoVideoMetadataList);
        verifyNoMoreInteractions(videoMetadataRepository);
        verifyNoMoreInteractions(youtubeAdapter);
        verifyNoMoreInteractions(vimeoAdapter);
    }

}
