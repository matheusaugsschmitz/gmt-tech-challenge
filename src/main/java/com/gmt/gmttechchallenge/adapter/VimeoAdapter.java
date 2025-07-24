package com.gmt.gmttechchallenge.adapter;

import com.gmt.gmttechchallenge.domain.VideoMetadata;
import com.gmt.gmttechchallenge.domain.VideoSource;
import com.gmt.gmttechchallenge.client.vimeo.MockedVimeoClient;
import com.gmt.gmttechchallenge.client.vimeo.VimeoVideoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VimeoAdapter implements VideoSourceInterface {

    @Getter
    private final VideoSource videoSource = VideoSource.VIMEO;

    private final MockedVimeoClient vimeoProxy;

    @Override
    public List<VideoMetadata> fetchBatchMetadata(List<String> ids) {
        if (ids == null || ids.isEmpty())
            return Collections.emptyList();

        List<Long> convertedIdsList = ids.stream()
                .map(String::trim)
                .filter(id -> id.matches("^\\d+$"))
                .map(Long::parseLong)
                .toList();

        if (convertedIdsList.isEmpty())
            return Collections.emptyList();

        List<VimeoVideoDto> vimeoVideos = vimeoProxy.fetchVideosByIdList(convertedIdsList);

        return vimeoVideos.stream()
                .map(video -> VideoMetadata.create(
                        String.valueOf(video.id()),
                        videoSource,
                        video.createdTime(),
                        video.tags(),
                        BigInteger.valueOf(video.viewCount()),
                        video.channelTitle(),
                        video.duration()
                )).toList();
    }
}