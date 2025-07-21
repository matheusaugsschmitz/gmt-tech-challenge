package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.client.vimeo.MockedVimeoClient;
import com.gmt.gmttechchallenge.client.vimeo.VimeoVideoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VimeoAdapter implements VideoSourceInterface {

    @Getter
    private final VideoSource videoSource = VideoSource.YOUTUBE;

    private final MockedVimeoClient vimeoProxy;

    @Override
    public List<VideoMetadata> importBatchMetadata(List<String> ids) {
        List<VimeoVideoDto> vimeoVideos = vimeoProxy.fetchVideosByIdList(ids.stream().map(Long::parseLong).toList());

        return vimeoVideos.stream()
                .map(video -> VideoMetadata.create(
                        String.valueOf(video.id()),
                        VideoSource.VIMEO,
                        video.createdTime(),
                        video.tags(),
                        BigInteger.valueOf(video.viewCount()),
                        video.channelTitle(),
                        video.duration()
                )).toList();
    }
}