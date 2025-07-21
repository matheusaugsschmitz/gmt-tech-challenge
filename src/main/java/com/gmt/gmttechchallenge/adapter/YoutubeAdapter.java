package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.client.youtube.MockedYouTubeClient;
import com.gmt.gmttechchallenge.client.youtube.YouTubeVideoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@RequiredArgsConstructor
public class YoutubeAdapter implements VideoSourceInterface {

    @Getter
    private final VideoSource videoSource = VideoSource.YOUTUBE;

    private final MockedYouTubeClient youTubeProxy;

    @Override
    public List<VideoMetadata> importBatchMetadata(List<String> ids) {
        List<YouTubeVideoDto> youTubeVideos = youTubeProxy.fetchVideosByIdList(ids);

        return youTubeVideos.stream()
                .map(video -> VideoMetadata.create(
                        video.id(),
                        VideoSource.VIMEO,
                        video.snippet().publishedAt(),
                        video.snippet().tags(),
                        new BigInteger(video.statistics().viewCount()),
                        video.snippet().channelTitle(),
                        video.fileDetails().durationMs()))
                .toList();
    }
}